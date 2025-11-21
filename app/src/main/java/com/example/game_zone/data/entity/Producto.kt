package com.example.game_zone.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "productos")
data class ProductoEntity(
    @PrimaryKey
    val id: Int,
    val nombre: String,
    val precio: Double,
    val descripcion: String,
    val categoria: String
)
