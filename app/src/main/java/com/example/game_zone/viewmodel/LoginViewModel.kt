package com.example.game_zone.viewmodel

import androidx.lifecycle.ViewModel
import com.example.game_zone.model.UsuarioUiState
import com.example.game_zone.model.UsuarioErrores
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class LoginViewModel : ViewModel() {

    private val _estado = MutableStateFlow(UsuarioUiState())
    val estado: StateFlow<UsuarioUiState> = _estado.asStateFlow()

    fun onCorreoChange(nuevoCorreo: String) {
        _estado.update { it.copy(correo = nuevoCorreo) }
    }

    fun onClaveChange(nuevaClave: String) {
        _estado.update { it.copy(clave = nuevaClave) }
    }

    fun validarLogin(): Boolean {
        val errores = UsuarioErrores(
            correo = if (_estado.value.correo.isBlank()) "El correo es requerido"
            else if (!_estado.value.correo.contains("@")) "Correo inválido"
            else null,
            clave = if (_estado.value.clave.isBlank()) "La contraseña es requerida"
            else if (_estado.value.clave.length < 6) "Mínimo 6 caracteres"
            else null
        )

        _estado.update { it.copy(errores = errores) }

        return errores.correo == null && errores.clave == null
    }
}