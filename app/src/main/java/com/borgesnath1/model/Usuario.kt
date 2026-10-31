package com.borgesnath1.model

data class Usuario (
    val id: String,
    var nome: String,
    var foto: String? = null,
    var sobre: String? = "",
    var contatos: List<Usuario>?
)