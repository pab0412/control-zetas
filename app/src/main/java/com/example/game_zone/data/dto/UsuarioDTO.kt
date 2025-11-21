package com.example.game_zone.data.dto

data class UsuarioDTO(
    val id: Int = 0,
    val nombre: String,
    val correo: String,
    val clave: String,
    val direccion: String,
    val aceptaterminos: Boolean,
    val gustos: String,  // Como String separado por comas
    val imagen: String?
)