package com.borgesnath1.model

import com.borgesnath1.enums.Status
import com.google.android.gms.maps.model.LatLng
import java.time.LocalDateTime

data class Evento (
    val id: String,   // id do documento no Firestore (eventos/{id})
    val nome: String,
    val data: LocalDateTime,
    val descricao: String? = null,
    val localizacao: LatLng? = null,
    val status: Status? = Status.AGUARDANDO,
    val proprietario: Usuario?,
    val convidados: List<Convidado>?
)