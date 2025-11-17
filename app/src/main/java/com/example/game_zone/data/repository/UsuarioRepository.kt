package com.example.game_zone.data.repository

import android.util.Log
import com.example.game_zone.data.dao.UsuarioDao
import com.example.game_zone.data.entity.UsuarioEntity
import com.example.game_zone.data.mapper.toDTO
import com.example.game_zone.data.mapper.toEntity
import com.example.game_zone.data.remote.RetrofitClient
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class UsuarioRepository(
    private val usuarioDao: UsuarioDao
) {
    private val apiService = RetrofitClient.instance

    // ===== MÉTODOS LOCALES (Room) =====

    suspend fun insertarUsuarioLocal(usuario: UsuarioEntity): Long {
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

    fun obtenerTodosLosUsuariosLocal(): Flow<List<UsuarioEntity>> {
        return usuarioDao.obtenerTodosLosUsuarios()
    }

    // ===== MÉTODOS CON API (Retrofit + Room Cache) =====

    // Obtener usuarios: primero de Room, luego actualizar desde API
    fun obtenerTodosLosUsuarios(): Flow<List<UsuarioEntity>> = flow {
        try {
            // 1. Emitir datos locales primero (rápido)
            val usuariosLocales = usuarioDao.obtenerTodosLosUsuarios()
            usuariosLocales.collect { emit(it) }

            // 2. Actualizar desde API en segundo plano
            try {
                val usuariosAPI = apiService.getAllUsuarios()
                val usuariosEntity = usuariosAPI.map { it.toEntity() }

                // 3. Actualizar cache local
                usuarioDao.eliminarTodos()
                usuariosEntity.forEach { usuarioDao.insertarUsuario(it) }

            } catch (e: Exception) {
                Log.e("UsuarioRepository", "Error al sincronizar con API: ${e.message}")
                // Los datos locales ya fueron emitidos
            }
        } catch (e: Exception) {
            Log.e("UsuarioRepository", "Error: ${e.message}")
        }
    }

    // Registrar usuario: guardar en API y luego en Room
    suspend fun registrarUsuario(usuario: UsuarioEntity): Result<UsuarioEntity> {
        return try {
            // 1. Guardar en API
            val usuarioDTO = usuario.toDTO()
            val nuevoUsuarioDTO = apiService.createUsuario(usuarioDTO)

            // 2. Guardar en Room
            val nuevoUsuario = nuevoUsuarioDTO.toEntity()
            usuarioDao.insertarUsuario(nuevoUsuario)

            Result.success(nuevoUsuario)
        } catch (e: Exception) {
            Log.e("UsuarioRepository", "Error al registrar: ${e.message}")
            Result.failure(e)
        }
    }

    // Sincronizar un usuario específico desde API
    suspend fun sincronizarUsuario(id: Int): Result<UsuarioEntity> {
        return try {
            val usuarioDTO = apiService.getUsuario(id)
            val usuarioEntity = usuarioDTO.toEntity()

            // Actualizar en Room
            usuarioDao.insertarUsuario(usuarioEntity)

            Result.success(usuarioEntity)
        } catch (e: Exception) {
            Log.e("UsuarioRepository", "Error al sincronizar usuario: ${e.message}")
            Result.failure(e)
        }
    }

    // Actualizar usuario en API y Room
    suspend fun actualizarUsuarioEnAPI(usuario: UsuarioEntity): Result<UsuarioEntity> {
        return try {
            val usuarioDTO = usuario.toDTO()
            val usuarioActualizado = apiService.updateUsuario(usuario.id, usuarioDTO)

            // Actualizar en Room
            val usuarioEntity = usuarioActualizado.toEntity()
            usuarioDao.actualizarUsuario(usuarioEntity)

            Result.success(usuarioEntity)
        } catch (e: Exception) {
            Log.e("UsuarioRepository", "Error al actualizar: ${e.message}")
            Result.failure(e)
        }
    }

    // Eliminar usuario de API y Room
    suspend fun eliminarUsuarioEnAPI(usuario: UsuarioEntity): Result<Boolean> {
        return try {
            apiService.deleteUsuario(usuario.id)
            usuarioDao.eliminarUsuario(usuario)
            Result.success(true)
        } catch (e: Exception) {
            Log.e("UsuarioRepository", "Error al eliminar: ${e.message}")
            Result.failure(e)
        }
    }
}