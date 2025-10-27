package com.example.game_zone.data.repository

import com.example.game_zone.data.dao.UsuarioDao
import com.example.game_zone.data.entity.UsuarioEntity
import kotlinx.coroutines.flow.Flow

class UsuarioRepository(
    private val usuarioDao: UsuarioDao
) {
    suspend fun insertarUsuario(usuario: UsuarioEntity): Long {
        return usuarioDao.insertarUsuario(usuario)
    }

    suspend fun actualizarUsuario(usuario: UsuarioEntity) {
        usuarioDao.actualizarUsuario(usuario)
    }

    suspend fun eliminarUsuario(usuario: UsuarioEntity) {
        usuarioDao.eliminarUsuario(usuario)
    }

    suspend fun obtenerUsuarioPorId(id: Int): UsuarioEntity? {
        return usuarioDao.obtenerUsuarioPorId(id)
    }

    suspend fun obtenerUsuarioPorCorreo(correo: String): UsuarioEntity? {
        return usuarioDao.obtenerUsuarioPorCorreo(correo)
    }

    suspend fun existeCorreo(correo: String): Boolean {
        return usuarioDao.existeCorreo(correo)
    }

    suspend fun login(correo: String, clave: String): UsuarioEntity? {
        return usuarioDao.login(correo, clave)
    }

    fun obtenerTodosLosUsuarios(): Flow<List<UsuarioEntity>> {
        return usuarioDao.obtenerTodosLosUsuarios()
    }
}