package com.example.game_zone.data.dao

import com.example.game_zone.data.entity.UsuarioEntity
import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface UsuarioDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertarUsuario(usuario: UsuarioEntity): Long

    @Update
    suspend fun actualizarUsuario(usuario: UsuarioEntity)

    @Delete
    suspend fun eliminarUsuario(usuario: UsuarioEntity)

    @Query("SELECT * FROM usuarios WHERE id = :id")
    suspend fun obtenerUsuarioPorId(id: Int): UsuarioEntity?

    @Query("SELECT * FROM usuarios WHERE correo = :correo")
    suspend fun obtenerUsuarioPorCorreo(correo: String): UsuarioEntity?

    @Query("SELECT * FROM usuarios WHERE correo = :correo AND clave = :clave")
    suspend fun login(correo: String, clave: String): UsuarioEntity?

    @Query("SELECT * FROM usuarios")
    fun obtenerTodosLosUsuarios(): Flow<List<UsuarioEntity>>

    @Query("DELETE FROM usuarios")
    suspend fun eliminarTodos()

    @Query("SELECT EXISTS(SELECT 1 FROM usuarios WHERE correo = :correo)")
    suspend fun existeCorreo(correo: String): Boolean
}
