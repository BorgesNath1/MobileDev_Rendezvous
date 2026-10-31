package com.borgesnath1

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.borgesnath1.enums.Status
import com.borgesnath1.model.Convidado
import com.borgesnath1.model.Evento
import com.borgesnath1.model.Usuario
import com.google.android.gms.maps.model.LatLng
import java.time.LocalDateTime

class MainViewModel() : ViewModel() {
    private val _user = mutableStateOf<Usuario?>(
        Usuario(
            id = "1",
            nome = "Ana Souza",
            sobre = "Amante de cafés especiais e bons encontros!",
            contatos = emptyList()
        )
    )

    private val _eventos = mutableStateListOf<Evento>().apply {
        addAll(getEventos(_user.value!!))
    }

    private val _eventoSelecionado = mutableStateOf<Evento?>(_eventos.firstOrNull())
    var eventoSelecionado: Evento?
        get() = _eventoSelecionado.value ?: _eventos.firstOrNull()
        set(value) { _eventoSelecionado.value = value }

    val user : Usuario?
        get() = _user.value

    val eventos get() = _eventos.toList()

    fun remove (evento: Evento){
        _eventos.remove(evento)
        if (_eventoSelecionado.value?.id == evento.id) {
            _eventoSelecionado.value = _eventos.firstOrNull()
        }
    }

    fun add (id: Int, nome: String, data: LocalDateTime, descricao:String, localizacao: LatLng, status: Status, proprietario: Usuario, convidados: List<Convidado>){
        val novoEvento = Evento(
            id = id,
            nome = nome,
            data = data,
            descricao = descricao,
            localizacao = localizacao,
            status = status,
            proprietario = proprietario,
            convidados = convidados
        )
        _eventos.add(0, novoEvento)
        _eventoSelecionado.value = novoEvento
    }

    fun atualizarStatus(evento: Evento, novoStatus: Status) {
        val index = _eventos.indexOfFirst { it.id == evento.id }
        if (index != -1) {
            val atualizado = _eventos[index].copy(status = novoStatus)
            _eventos[index] = atualizado
            _eventoSelecionado.value = atualizado
        }
    }

    fun adicionarConvidado(evento: Evento, nomeOuEmail: String) {
        val index = _eventos.indexOfFirst { it.id == evento.id }
        if (index != -1 && nomeOuEmail.isNotBlank()) {
            val novoConvidado = Convidado(
                usuario = Usuario(id = "${System.currentTimeMillis()}", nome = nomeOuEmail, contatos = emptyList()),
                status = Status.AGUARDANDO
            )
            val listaAtual = _eventos[index].convidados ?: emptyList()
            val atualizado = _eventos[index].copy(convidados = listaAtual + novoConvidado)
            _eventos[index] = atualizado
            _eventoSelecionado.value = atualizado
        }
    }

    fun atualizarLocalizacao(evento: Evento, novaLocalizacao: LatLng) {
        val index = _eventos.indexOfFirst { it.id == evento.id }
        if (index != -1) {
            val atualizado = _eventos[index].copy(localizacao = novaLocalizacao)
            _eventos[index] = atualizado
            _eventoSelecionado.value = atualizado
        }
    }
}

private fun getEventos (criador: Usuario): List<Evento> {
    val convidadosPadrao = listOf(
        Convidado(Usuario("1", "Ana Souza", contatos = emptyList()), Status.CONFIRMADO),
        Convidado(Usuario("2", "Rafael Lima", contatos = emptyList()), Status.CONFIRMADO),
        Convidado(Usuario("3", "Camila Torres", contatos = emptyList()), Status.CONFIRMADO),
        Convidado(Usuario("4", "Bruno Mendes", contatos = emptyList()), Status.AGUARDANDO)
    )
    return listOf(
        Evento(
            id = 1,
            nome = "Café da Manhã no Bom Dia",
            data = LocalDateTime.now().plusDays(3),
            descricao = "Café da Manhã • Café Bom Dia, Pinheiros",
            localizacao = LatLng(-8.0522, -34.8951),
            status = Status.CONFIRMADO,
            proprietario = criador,
            convidados = convidadosPadrao
        ),
        Evento(
            id = 2,
            nome = "Papo de tarde na Figueira",
            data = LocalDateTime.now().plusDays(5),
            descricao = "Tarde • Café Figueira, Boa Viagem",
            localizacao = LatLng(-8.1190, -34.9045),
            status = Status.AGUARDANDO,
            proprietario = criador,
            convidados = convidadosPadrao.take(2)
        ),
        Evento(
            id = 3,
            nome = "Happy Hour & Espresso",
            data = LocalDateTime.now().plusDays(7),
            descricao = "Happy Hour • Recife Antigo",
            localizacao = LatLng(-8.0631, -34.8711),
            status = Status.CONFIRMADO,
            proprietario = criador,
            convidados = convidadosPadrao.take(3)
        )
    )
}
