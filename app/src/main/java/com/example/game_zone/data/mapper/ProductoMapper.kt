package com.example.game_zone.data.mapper

import com.example.game_zone.data.dto.ProductoDTO
import com.example.game_zone.data.entity.ProductoEntity

fun ProductoDTO.toEntity(): ProductoEntity {
    return ProductoEntity(
        id = this.id,
        nombre = this.nombre,
        precio = this.precio,
        descripcion = this.descripcion,
        categoria = this.categoria
    )
}

fun ProductoEntity.toDTO(): ProductoDTO {
    return ProductoDTO(
        id = this.id,
        nombre = this.nombre,
        precio = this.precio,
        descripcion = this.descripcion,
        categoria = this.categoria
    )
}