package com.example.game_zone.viewmodel

import android.util.Log
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

    // REGISTRAR USUARIO (CON API)
    fun registrarUsuario() {
        viewModelScope.launch {
            _cargando.value = true
            try {
                if (!validarFormulario()) {
                    _cargando.value = false
                    return@launch
                }

                // Verificar si el correo ya existe localmente
                if (repository.existeCorreo(_estado.value.correo)) {
                    _estado.update {
                        it.copy(errores = it.errores.copy(correo = "Este correo ya está registrado"))
                    }
                    _cargando.value = false
                    return@launch
                }

                // Registrar en API y Room
                val entity = _estado.value.toEntity()
                val resultado = repository.registrarUsuario(entity)

                resultado.onSuccess { usuarioGuardado ->
                    _usuarioActualId.value = usuarioGuardado.id
                    _registroExitoso.value = true
                    Log.d("UsuarioViewModel", "Usuario registrado con éxito: ${usuarioGuardado.id}")
                }.onFailure { error ->
                    Log.e("UsuarioViewModel", "Error al registrar: ${error.message}")
                    _estado.update {
                        it.copy(errores = it.errores.copy(correo = "Error al registrar usuario en el servidor"))
                    }
                }
            } catch (e: Exception) {
                Log.e("UsuarioViewModel", "Error inesperado: ${e.message}")
                _estado.update {
                    it.copy(errores = it.errores.copy(correo = "Error al registrar usuario"))
                }
            } finally {
                _cargando.value = false
            }
        }
    }

    // LOGIN (API)
    fun login(correo: String, clave: String) {
        viewModelScope.launch {
            _cargando.value = true
            try {
                val resultado = repository.loginConAPI(correo, clave)

                resultado.onSuccess { usuario ->
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
                }.onFailure { exception ->
                    _estado.update {
                        it.copy(
                            errores = it.errores.copy(
                                correo = when {
                                    exception.message?.contains("Credenciales") == true ->
                                        "Correo o contraseña incorrectos"
                                    else -> "Error al iniciar sesión. Verifica tu conexión"
                                }
                            )
                        )
                    }
                }
            } catch (e: Exception) {
                _estado.update {
                    it.copy(errores = it.errores.copy(correo = "Error inesperado al iniciar sesión"))
                }
            } finally {
                _cargando.value = false
            }
        }
    }

    // ACTUALIZAR USUARIO (CON API)
    fun actualizarUsuario() {
        viewModelScope.launch {
            _cargando.value = true
            try {
                if (!validarFormulario()) {
                    _cargando.value = false
                    return@launch
                }

                val id = _usuarioActualId.value ?: return@launch
                val entity = _estado.value.toEntity(id)

                val resultado = repository.actualizarUsuarioEnAPI(entity)

                resultado.onSuccess {
                    Log.d("UsuarioViewModel", "Usuario actualizado con éxito")
                }.onFailure { error ->
                    Log.e("UsuarioViewModel", "Error al actualizar: ${error.message}")
                    _estado.update {
                        it.copy(errores = it.errores.copy(correo = "Error al actualizar usuario"))
                    }
                }
            } catch (e: Exception) {
                _estado.update {
                    it.copy(errores = it.errores.copy(correo = "Error al actualizar usuario"))
                }
            } finally {
                _cargando.value = false
            }
        }
    }

    // CARGAR USUARIO
    fun cargarUsuario(id: Int) {
        viewModelScope.launch {
            _cargando.value = true
            try {
                // Primero intentar obtener de Room
                val usuario = repository.obtenerUsuarioPorId(id)
                usuario?.let {
                    _estado.value = it.toUiState()
                    _usuarioActualId.value = it.id
                }

                // Luego sincronizar con API en segundo plano
                repository.sincronizarUsuario(id).onSuccess { usuarioActualizado ->
                    _estado.value = usuarioActualizado.toUiState()
                }
            } catch (e: Exception) {
                Log.e("UsuarioViewModel", "Error al cargar usuario: ${e.message}")
            } finally {
                _cargando.value = false
            }
        }
    }

    // OBTENER TODOS LOS USUARIOS (OPCIONAL - PARA DEBUGGING)
    fun cargarTodosLosUsuarios() {
        viewModelScope.launch {
            repository.obtenerTodosLosUsuarios().collect { usuarios ->
                Log.d("UsuarioViewModel", "Usuarios cargados: ${usuarios.size}")
                // Aquí puedes hacer algo con la lista si lo necesitas
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