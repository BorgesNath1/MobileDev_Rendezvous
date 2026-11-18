package com.borgesnath1.db.fb

import com.borgesnath1.model.Usuario
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.firestore

class FBDatabase {
    interface Listener {
        fun onUserLoaded(user: Usuario)
        fun onUserSignOut()
    }

    private val auth = Firebase.auth
    private val db = Firebase.firestore
    private var userReg: ListenerRegistration? = null
    private var listener : Listener? = null

    init {
        auth.addAuthStateListener { auth ->
            userReg?.remove()
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
}
