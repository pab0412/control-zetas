data class `UsuarioUiState`(
    val nombre: String = "",
    val correo: String = "",
    val clave: String = "",
    val direccion: String = "",
    val aceptaterminos: Boolean = false,
    val errores: UsuarioErrores = UsuarioErrores()

)
