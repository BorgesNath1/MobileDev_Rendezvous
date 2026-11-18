package com.borgesnath1

import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.borgesnath1.db.fb.FBDatabase
import com.borgesnath1.db.fb.toFBEvento
import com.borgesnath1.enums.Status
import com.borgesnath1.model.Convidado
import com.borgesnath1.model.Evento
import com.borgesnath1.model.Usuario
import com.google.android.gms.maps.model.LatLng
import java.time.LocalDateTime

class MainViewModel(private val db: FBDatabase) : ViewModel(), FBDatabase.Listener {
    // Usuário logado, carregado do Firestore (users/{uid})
    private val _user = mutableStateOf<Usuario?>(null)

    // Encontros vindos do Firestore; um map id -> encontro facilita atualizar um encontro
    private val _eventos = mutableStateMapOf<String, Evento>()

    // Guarda só o id: quando o encontro muda no Firestore, a tela de detalhes mostra a versão nova
    private val _eventoSelecionadoId = mutableStateOf<String?>(null)
    var eventoSelecionado: Evento?
        get() = _eventoSelecionadoId.value?.let { _eventos[it] } ?: eventos.firstOrNull()
        set(value) { _eventoSelecionadoId.value = value?.id }

    val user : Usuario?
        get() = _user.value

    // Mais próximo primeiro (a Home mostra o primeiro como "Próximo encontro")
    val eventos : List<Evento>
        get() = _eventos.values.sortedBy { it.data }

    init {
        db.setListener(this)
    }

    override fun onUserLoaded(user: Usuario) {
        _user.value = user
    }

    override fun onUserSignOut() {
        _user.value = null
        _eventos.clear()
        _eventoSelecionadoId.value = null
    }

    override fun onEventoAdded(evento: Evento) {
        _eventos[evento.id] = evento
    }

    override fun onEventoUpdated(evento: Evento) {
        _eventos[evento.id] = evento
    }

    override fun onEventoRemoved(evento: Evento) {
        _eventos.remove(evento.id)
    }

    fun atualizarPerfil(nome: String, sobre: String) {
        db.atualizarPerfil(nome, sobre)
    }

    // Só o dono apaga; para convidados não faz nada
    fun remove (evento: Evento){
        db.remove(evento)
    }

    fun add (nome: String, data: LocalDateTime, descricao:String, localizacao: LatLng, status: Status, proprietario: Usuario, convidados: List<Convidado>){
        val novoEvento = Evento(
            id = "",
            nome = nome,
            data = data,
            descricao = descricao,
            localizacao = localizacao,
            status = status,
            proprietario = proprietario,
            convidados = convidados
        )
        _eventoSelecionadoId.value = db.add(novoEvento.toFBEvento())
    }

    fun atualizarStatus(evento: Evento, novoStatus: Status) {
        db.atualizarStatus(evento, novoStatus)
    }

    // Convites (por código/link e pela lista de contatos) entram no PR feat/convites
    @Suppress("UNUSED_PARAMETER")
    fun adicionarConvidado(evento: Evento, nomeOuEmail: String) {
    }

    fun atualizarLocalizacao(evento: Evento, novaLocalizacao: LatLng) {
        db.atualizarLocalizacao(evento, novaLocalizacao)
    }
}

class MainViewModelFactory(private val db : FBDatabase) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return MainViewModel(db) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
