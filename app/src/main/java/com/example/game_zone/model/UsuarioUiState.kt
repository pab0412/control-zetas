package com.example.game_zone.model

data class UsuarioUiState(
    val nombre: String = "",
    val correo: String = "",
    val clave: String = "",
    val direccion: String = "",
    val aceptaterminos: Boolean = false,
    val gustos: List<String> = emptyList(),
    val imagen: String? = null,
    val errores: UsuarioErrores = UsuarioErrores()
)

object GustosDisponibles {
    val lista = listOf(
        "Consolas",
        "Juegos Físicos",
        "Accesorios Gaming",
        "Auriculares/Headsets",
        "Teclados y Ratones",
        "Sillas Gamer",
        "Monitores Gaming",
    )
}