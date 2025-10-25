package com.example.game_zone.view_model

import androidx.lifecycle.ViewModel
import com.example.game_zone.model.UsuarioUiState
import com.example.game_zone.model.UsuarioErrores
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

class UsuarioViewModel : ViewModel(){
    private  val _estado = MutableStateFlow(UsuarioUiState())

    val estado: StateFlow<UsuarioUiState> = _estado

    fun onNombreChange(valor: String){
        _estado.update { it.copy(nombre = valor, errores = it.errores.copy(nombre = null)) }
    }

    fun onCorreoChange(valor: String){
        _estado.update { it.copy(correo = valor, errores = it.errores.copy(correo = null)) }
    }

    fun onClaveChange(valor: String){
        _estado.update { it.copy(clave = valor, errores = it.errores.copy(clave = null)) }
    }

    fun onDireccionChange(valor: String){
        _estado.update { it.copy(direccion = valor, errores = it.errores.copy(direccion = null))}
    }
    fun onAceptarTerminosChange(valor: Boolean){
        _estado.update {it.copy(aceptaterminos = valor) }
    }

    fun onGustoChange(gusto: String, seleccionado:Boolean){
        _estado.update {
            val nuevosGustos = if(seleccionado) it.gustos + gusto else it.gustos - gusto
            it.copy(gustos = nuevosGustos)
        }
    }

    fun onImagenChange(uri: String){
        _estado.update { it.copy(imagen = uri) }
    }


    fun validarFormulario() : Boolean{
        val estadoActual = _estado.value
        val errores = UsuarioErrores(
            nombre = if (estadoActual.nombre.isBlank()) "Campo Obligatorio" else null,
            correo = if (!estadoActual.correo.contains("@")) "Correo Invalido" else null,
            clave = if (estadoActual.clave.length < 6) "Clave debe tener al menos 6 digitos" else null,
            direccion = if(estadoActual.direccion.isBlank()) "Campo Obligatorio" else null,
            gustos = if(estadoActual.gustos.isEmpty()) "Seleccione al menos un gusto" else null
        )

        val hayErrores = listOfNotNull(errores.nombre, errores.correo, errores.clave, errores.direccion, errores.gustos).isNotEmpty()

        _estado.update {it.copy(errores = errores)}

        return !hayErrores
    }

}