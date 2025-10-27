package com.example.game_zone.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "usuarios")
data class UsuarioEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val nombre: String,
    val correo: String,
    val clave: String,
    val direccion: String,
    val aceptaterminos: Boolean,
    val gustos: String,
    val imagen: String?
)
