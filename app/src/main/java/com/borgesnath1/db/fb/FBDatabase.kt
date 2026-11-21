package com.borgesnath1.db.fb

import android.util.Log
import com.borgesnath1.enums.Status
import com.borgesnath1.model.Evento
import com.borgesnath1.model.Usuario
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.firestore

private const val TAG = "FBDatabase"

class FBDatabase {
    interface Listener {
        fun onUserLoaded(user: Usuario)
        fun onUserSignOut()
        fun onEventoAdded(evento: Evento)
        fun onEventoUpdated(evento: Evento)
        fun onEventoRemoved(evento: Evento)
    }

    private val auth = Firebase.auth
    private val db = Firebase.firestore
    private var userReg: ListenerRegistration? = null
    private var eventosReg: ListenerRegistration? = null
    private var listener : Listener? = null

    init {
        auth.addAuthStateListener { auth ->
            userReg?.remove()
            eventosReg?.remove()
            val uid = auth.currentUser?.uid
            if (uid == null) {
                listener?.onUserSignOut()
                return@addAuthStateListener
            }
            // Snapshot listener (e não get()): logo após o cadastro o documento ainda
            // não existe; quando register() gravar, o listener dispara de novo.
            // Também traz de volta as edições do perfil.
            userReg = db.collection("users").document(uid)
                .addSnapshotListener { snapshot, ex ->
                    if (ex != null) return@addSnapshotListener
                    snapshot?.toObject(FBUsuario::class.java)?.let { fbUsuario ->
                        listener?.onUserLoaded(fbUsuario.toUsuario(uid))
                    }
                }
            // "Meus encontros": todos em que o uid está na lista membrosUids (dono ou convidado)
            eventosReg = db.collection("eventos")
                .whereArrayContains("membrosUids", uid)
                .addSnapshotListener { snapshots, ex ->
                    if (ex != null) {
                        Log.w(TAG, "Erro ao ouvir os encontros", ex)
                        return@addSnapshotListener
                    }
                    snapshots?.documentChanges?.forEach { change ->
                        val evento = change.document.toObject(FBEvento::class.java)
                            .toEvento(change.document.id)
                        when (change.type) {
                            DocumentChange.Type.ADDED -> listener?.onEventoAdded(evento)
                            DocumentChange.Type.MODIFIED -> listener?.onEventoUpdated(evento)
                            DocumentChange.Type.REMOVED -> listener?.onEventoRemoved(evento)
                        }
                    }
                }
        }
    }

    fun setListener(listener: Listener? = null) {
        this.listener = listener
    }

    fun register(user: FBUsuario) {
        if (auth.currentUser == null) throw RuntimeException("User not logged in!")
        val uid = auth.currentUser!!.uid
        db.collection("users").document(uid).set(user)
    }

    fun atualizarPerfil(nome: String, sobre: String) {
        if (auth.currentUser == null) throw RuntimeException("User not logged in!")
        val uid = auth.currentUser!!.uid
        db.collection("users").document(uid).update("nome", nome, "sobre", sobre)
    }

    // O Firestore gera o id do documento (devolvido para a tela poder selecionar o encontro);
    // o listener avisa quando o encontro chegar
    fun add(evento: FBEvento): String {
        if (auth.currentUser == null) throw RuntimeException("User not logged in!")
        val ref = db.collection("eventos").document()
        ref.set(evento).addOnFailureListener { Log.w(TAG, "Erro ao criar encontro", it) }
        return ref.id
    }

    // Só o dono apaga (as regras do Firestore também garantem isso)
    fun remove(evento: Evento) {
        val uid = auth.currentUser?.uid ?: throw RuntimeException("User not logged in!")
        if (evento.proprietario?.id != uid) return
        db.collection("eventos").document(evento.id).delete()
    }

    // Dono: muda o status do encontro.
    // Convidado: muda só o próprio status; se todos confirmarem, o encontro vira CONFIRMADO.
    // Usa transação porque lê os status de todos antes de decidir.
    fun atualizarStatus(evento: Evento, status: Status) {
        val uid = auth.currentUser?.uid ?: throw RuntimeException("User not logged in!")
        val ref = db.collection("eventos").document(evento.id)
        if (evento.proprietario?.id == uid) {
            ref.update("status", status.name)
            return
        }
        db.runTransaction { transaction ->
            val fbEvento = transaction.get(ref).toObject(FBEvento::class.java)
                ?: return@runTransaction
            if (!fbEvento.convidados.containsKey(uid)) return@runTransaction
            val statusDeTodos = fbEvento.convidados.mapValues { it.value.status } + (uid to status.name)
            val updates = mutableMapOf<String, Any>("convidados.$uid.status" to status.name)
            if (statusDeTodos.values.all { it == Status.CONFIRMADO.name }) {
                updates["status"] = Status.CONFIRMADO.name
            }
            transaction.update(ref, updates)
        }
    }

    // Local do encontro (clique no mapa dos detalhes): só o dono altera
    fun atualizarLocalizacao(evento: Evento, localizacao: LatLng) {
        val uid = auth.currentUser?.uid ?: throw RuntimeException("User not logged in!")
        if (evento.proprietario?.id != uid) return
        db.collection("eventos").document(evento.id)
            .update("lat", localizacao.latitude, "lng", localizacao.longitude)
    }
}
