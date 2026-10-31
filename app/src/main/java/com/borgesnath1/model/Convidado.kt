package com.borgesnath1.model

import com.borgesnath1.enums.Status
import com.google.android.gms.maps.model.LatLng

data class Convidado(
    var usuario: Usuario,
    var status: Status,
    var localizacao: LatLng? = null
    )