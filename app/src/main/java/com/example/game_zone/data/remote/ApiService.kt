package com.example.game_zone.data.remote

import com.example.game_zone.data.model.UsuarioDTO
import retrofit2.Response
import retrofit2.http.*

interface ApiService {

    @GET("usuarios")
    suspend fun getAllUsuarios(): List<UsuarioDTO>

    @GET("usuarios/{id}")
    suspend fun getUsuario(@Path("id") id: Int): UsuarioDTO

    @GET("usuarios/correo/{correo}")
    suspend fun getUsuarioPorCorreo(@Path("correo") correo: String): UsuarioDTO

    @POST("usuarios")
    suspend fun createUsuario(@Body usuario: UsuarioDTO): UsuarioDTO

    @PUT("usuarios/{id}")
    suspend fun updateUsuario(@Path("id") id: Int, @Body usuario: UsuarioDTO): UsuarioDTO

    @DELETE("usuarios/{id}")
    suspend fun deleteUsuario(@Path("id") id: Int): Response<Void>
}