package com.borgesnath1.db.fb

import com.borgesnath1.enums.Status
import com.borgesnath1.model.Convidado
import com.borgesnath1.model.Evento
import com.borgesnath1.model.Usuario
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.Timestamp
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.Date

// Um convidado dentro do mapa eventos/{id}.convidados (a chave do mapa é o uid)
class FBConvidado {
    var nome : String? = null
    var status : String? = null
}

// Formato do documento eventos/{id} no Firestore
class FBEvento {
    var nome : String? = null
    var descricao : String? = null
    var data : Timestamp? = null
    var lat : Double? = null
    var lng : Double? = null
    var status : String? = null
    var donoUid : String? = null
    var donoNome : String? = null
    // Só um índice para a busca "meus encontros" (whereArrayContains) e para as regras:
    // dono + convidados. Quem guarda nome e status de cada um é o mapa convidados.
    var membrosUids : List<String> = emptyList()
    var convidados : Map<String, FBConvidado> = emptyMap()

    fun toEvento(id: String): Evento {
        val latlng = if (lat != null && lng != null) LatLng(lat!!, lng!!) else null
        val dataLocal = data?.let {
            LocalDateTime.ofInstant(it.toDate().toInstant(), ZoneId.systemDefault())
        } ?: LocalDateTime.now()
        return Evento(
            id = id,
            nome = nome ?: "",
            data = dataLocal,
            descricao = descricao,
            localizacao = latlng,
            status = status.toStatus(),
            proprietario = Usuario(id = donoUid ?: "", nome = donoNome ?: "", contatos = emptyList()),
            convidados = convidados.map { (uid, convidado) ->
                Convidado(
                    usuario = Usuario(id = uid, nome = convidado.nome ?: "", contatos = emptyList()),
                    status = convidado.status.toStatus()
                )
            }
        )
    }
}

fun Evento.toFBEvento() : FBEvento {
    val fbEvento = FBEvento()
    fbEvento.nome = this.nome
    fbEvento.descricao = this.descricao
    fbEvento.data = Timestamp(Date.from(this.data.atZone(ZoneId.systemDefault()).toInstant()))
    fbEvento.lat = this.localizacao?.latitude
    fbEvento.lng = this.localizacao?.longitude
    fbEvento.status = (this.status ?: Status.AGUARDANDO).name
    fbEvento.donoUid = this.proprietario?.id
    fbEvento.donoNome = this.proprietario?.nome
    val convidados = this.convidados ?: emptyList()
    fbEvento.convidados = convidados.associate { convidado ->
        val fbConvidado = FBConvidado()
        fbConvidado.nome = convidado.usuario.nome
        fbConvidado.status = convidado.status.name
        convidado.usuario.id to fbConvidado
    }
    fbEvento.membrosUids = (listOfNotNull(this.proprietario?.id) + convidados.map { it.usuario.id }).distinct()
    return fbEvento
}

private fun String?.toStatus(): Status =
    Status.entries.firstOrNull { it.name == this } ?: Status.AGUARDANDO
