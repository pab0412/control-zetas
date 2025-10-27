package com.example.game_zone.data.mapper

import com.example.game_zone.data.entity.UsuarioEntity
import com.example.game_zone.model.UsuarioUiState
import com.example.game_zone.ui.utils.converter.GustosConverter

fun UsuarioEntity.toUiState(): UsuarioUiState {
    return UsuarioUiState(
        nombre = nombre,
        correo = correo,
        clave = clave,
        direccion = direccion,
        aceptaterminos = aceptaterminos,
        gustos = GustosConverter.jsonToList(gustos),
        imagen = imagen
    )
}

fun UsuarioUiState.toEntity(id: Int = 0): UsuarioEntity {
    return UsuarioEntity(
        id = id,
        nombre = nombre,
        correo = correo,
        clave = clave,
        direccion = direccion,
        aceptaterminos = aceptaterminos,
        gustos = GustosConverter.listToJson(gustos),
        imagen = imagen
    )
}