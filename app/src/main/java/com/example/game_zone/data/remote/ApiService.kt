package com.example.game_zone.data.remote

import com.example.game_zone.data.dto.UsuarioDTO
import com.example.game_zone.data.dto.ProductoDTO
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

    @GET("usuarios/auth/login")
    suspend fun login(
        @Query("correo") correo: String,
        @Query("clave") clave: String
    ): Response<UsuarioDTO>

    @PUT("usuarios/{id}")
    suspend fun updateUsuario(@Path("id") id: Int, @Body usuario: UsuarioDTO): UsuarioDTO

    @DELETE("usuarios/{id}")
    suspend fun deleteUsuario(@Path("id") id: Int): Response<Void>

    // Productos
    @GET("productos")
    suspend fun obtenerTodosProductos(): List<ProductoDTO>

    @GET("productos/{id}")
    suspend fun obtenerProductoPorId(@Path("id") id: Int): ProductoDTO

    @GET("productos/categoria/{categoria}")
    suspend fun obtenerProductosPorCategoria(@Path("categoria") categoria: String): List<ProductoDTO>

    @GET("productos/buscar/{nombre}")
    suspend fun buscarProductos(@Path("nombre") nombre: String): List<ProductoDTO>

    @POST("productos")
    suspend fun crearProducto(@Body producto: ProductoDTO): ProductoDTO

    @PUT("productos/{id}")
    suspend fun actualizarProducto(@Path("id") id: Int, @Body producto: ProductoDTO): ProductoDTO

    @DELETE("productos/{id}")
    suspend fun eliminarProducto(@Path("id") id: Int)
}
