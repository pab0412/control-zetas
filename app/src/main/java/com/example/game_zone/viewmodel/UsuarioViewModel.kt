package com.example.game_zone.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.game_zone.data.mapper.toEntity
import com.example.game_zone.data.mapper.toUiState
import com.example.game_zone.data.repository.UsuarioRepository
import com.example.game_zone.model.UsuarioUiState
import com.example.game_zone.model.UsuarioErrores
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class UsuarioViewModel(
    private val repository: UsuarioRepository
) : ViewModel() {

    private val _estado = MutableStateFlow(UsuarioUiState())
    val estado: StateFlow<UsuarioUiState> = _estado

    private val _registroExitoso = MutableStateFlow(false)
    val registroExitoso: StateFlow<Boolean> = _registroExitoso

    private val _loginExitoso = MutableStateFlow(false)
    val loginExitoso: StateFlow<Boolean> = _loginExitoso

    private val _usuarioActualId = MutableStateFlow<Int?>(null)
    val usuarioActualId: StateFlow<Int?> = _usuarioActualId

    private val _cargando = MutableStateFlow(false)
    val cargando: StateFlow<Boolean> = _cargando

    // Funciones de cambio de estado
    fun onNombreChange(valor: String) {
        _estado.update { it.copy(nombre = valor, errores = it.errores.copy(nombre = null)) }
    }

    fun onCorreoChange(valor: String) {
        _estado.update { it.copy(correo = valor, errores = it.errores.copy(correo = null)) }
    }

    fun onClaveChange(valor: String) {
        _estado.update { it.copy(clave = valor, errores = it.errores.copy(clave = null)) }
    }

    fun onDireccionChange(valor: String) {
        _estado.update { it.copy(direccion = valor, errores = it.errores.copy(direccion = null)) }
    }

    fun onAceptarTerminosChange(valor: Boolean) {
        _estado.update { it.copy(aceptaterminos = valor) }
    }

    fun onGustoChange(gusto: String, seleccionado: Boolean) {
        _estado.update {
            val nuevosGustos = if (seleccionado) it.gustos + gusto else it.gustos - gusto
            it.copy(gustos = nuevosGustos)
        }
    }

    fun onImagenChange(uri: String) {
        _estado.update { it.copy(imagen = uri) }
    }

    // Validación
    fun validarFormulario(): Boolean {
        val estadoActual = _estado.value
        val errores = UsuarioErrores(
            nombre = if (estadoActual.nombre.isBlank()) "Campo Obligatorio" else null,
            correo = if (!estadoActual.correo.contains("@")) "Correo Invalido" else null,
            clave = if (estadoActual.clave.length < 6) "Clave debe tener al menos 6 digitos" else null,
            direccion = if (estadoActual.direccion.isBlank()) "Campo Obligatorio" else null,
            gustos = if (estadoActual.gustos.isEmpty()) "Seleccione al menos un gusto" else null
        )

        val hayErrores = listOfNotNull(
            errores.nombre,
            errores.correo,
            errores.clave,
            errores.direccion,
            errores.gustos
        ).isNotEmpty()

        _estado.update { it.copy(errores = errores) }

        return !hayErrores
    }

    // Nuevas funciones para interactuar con la BD
    fun registrarUsuario() {
        viewModelScope.launch {
            try {
                if (!validarFormulario()) {
                    return@launch
                }

                // Verificar si el correo ya existe
                if (repository.existeCorreo(_estado.value.correo)) {
                    _estado.update {
                        it.copy(errores = it.errores.copy(correo = "Este correo ya está registrado"))
                    }
                    return@launch
                }

                val entity = _estado.value.toEntity()
                val id = repository.insertarUsuario(entity)
                _usuarioActualId.value = id.toInt()
                _registroExitoso.value = true
            } catch (e: Exception) {
                _estado.update {
                    it.copy(errores = it.errores.copy(correo = "Error al registrar usuario"))
                }
            }
        }
    }

    fun login(correo: String, clave: String) {
        viewModelScope.launch {
            _cargando.value = true
            try {
                val usuario = repository.login(correo, clave)
                if (usuario != null) {
                    _estado.update {
                        it.copy(
                            nombre = usuario.nombre,
                            correo = usuario.correo,
                            clave = usuario.clave,
                            direccion = usuario.direccion,
                            gustos = usuario.gustos.split(",").map { it.trim() },
                            imagen = usuario.imagen,
                            aceptaterminos = true,
                            errores = UsuarioErrores()
                        )
                    }
                    _usuarioActualId.value = usuario.id
                    _loginExitoso.value = true
                } else {
                    _estado.update {
                        it.copy(errores = it.errores.copy(correo = "Correo o contraseña incorrectos"))
                    }
                }
            } catch (e: Exception) {
                _estado.update {
                    it.copy(errores = it.errores.copy(correo = "Error al iniciar sesión"))
                }
            } finally {
                _cargando.value = false
            }
        }
    }

    fun actualizarUsuario() {
        viewModelScope.launch {
            try {
                if (!validarFormulario()) {
                    return@launch
                }

                val id = _usuarioActualId.value ?: return@launch
                val entity = _estado.value.toEntity(id)
                repository.actualizarUsuario(entity)
            } catch (e: Exception) {
                _estado.update {
                    it.copy(errores = it.errores.copy(correo = "Error al actualizar usuario"))
                }
            }
        }
    }

    fun cargarUsuario(id: Int) {
        viewModelScope.launch {
            try {
                val usuario = repository.obtenerUsuarioPorId(id)
                usuario?.let {
                    _estado.value = it.toUiState()
                    _usuarioActualId.value = it.id
                }
            } catch (e: Exception) {
            }
        }
    }

    fun cerrarSesion() {
        _usuarioActualId.value = null
        _loginExitoso.value = false
        limpiarFormulario()
    }

    fun limpiarFormulario() {
        _estado.value = UsuarioUiState()
        _registroExitoso.value = false
    }

    fun resetRegistroExitoso() {
        _registroExitoso.value = false
    }

    fun resetLoginExitoso() {
        _loginExitoso.value = false
    }

    // Limpiar errores
    fun limpiarErrores() {
        _estado.update {
            it.copy(
                errores = UsuarioErrores()
            )
        }
    }
    // View Factory para interaccion con base de datos
    class UsuarioViewModelFactory(
        private val repository: UsuarioRepository
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(UsuarioViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return UsuarioViewModel(repository) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}
