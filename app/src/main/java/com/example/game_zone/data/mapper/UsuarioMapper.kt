package com.example.game_zone.data.mapper

import com.example.game_zone.data.entity.UsuarioEntity
import com.example.game_zone.data.model.UsuarioDTO
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

fun UsuarioDTO.toEntity(): UsuarioEntity {
    return UsuarioEntity(
        id = id,
        nombre = nombre,
        correo = correo,
        clave = clave,
        direccion = direccion,
        aceptaterminos = aceptaterminos,
        gustos = gustos,
        imagen = imagen
    )
}

// Entity -> DTO (NUEVO)
fun UsuarioEntity.toDTO(): UsuarioDTO {
    return UsuarioDTO(
        id = id,
        nombre = nombre,
        correo = correo,
        clave = clave,
        direccion = direccion,
        aceptaterminos = aceptaterminos,
        gustos = gustos,
        imagen = imagen
    )
}

fun UsuarioUiState.toDTO(id: Int = 0): UsuarioDTO {
    return UsuarioDTO(
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