package com.borgesnath1.db.fb

import com.borgesnath1.model.Usuario

// Formato do documento users/{uid} no Firestore (precisa de construtor vazio e vars)
class FBUsuario {
    var nome : String? = null
    var email : String? = null
    var foto : String? = null
    var sobre : String? = null

    fun toUsuario(uid: String): Usuario {
        return Usuario(
            id = uid,
            nome = nome ?: "",
            email = email,
            foto = foto,
            sobre = sobre ?: "",
            contatos = emptyList()
        )
    }
}

fun Usuario.toFBUsuario() : FBUsuario {
    val fbUsuario = FBUsuario()
    fbUsuario.nome = this.nome
    fbUsuario.email = this.email
    fbUsuario.foto = this.foto
    fbUsuario.sobre = this.sobre
    return fbUsuario
}
