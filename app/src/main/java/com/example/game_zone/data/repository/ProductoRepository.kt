package com.example.game_zone.data.repository

import android.util.Log
import com.example.game_zone.data.dao.ProductoDao
import com.example.game_zone.data.entity.ProductoEntity
import com.example.game_zone.data.mapper.toEntity
import com.example.game_zone.data.remote.ApiService
import com.example.game_zone.data.remote.RetrofitClient
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class ProductoRepository(
    private val productoDao: ProductoDao,
) {
    private val apiService = RetrofitClient.instance

    // Obtener todos los productos: primero de Room, luego actualizar desde API
    fun obtenerTodosLosProductos(): Flow<List<ProductoEntity>> = flow {
        try {
            try {
                val productosAPI = apiService.obtenerTodosProductos()

                val productosEntity = productosAPI.map { it.toEntity() }

                // Guardar en Room
                productoDao.eliminarTodos()
                productoDao.insertarProductos(productosEntity)

                // Emitir los productos de la API
                emit(productosEntity)

            } catch (apiError: Exception) {
                apiError.printStackTrace()

                // Si falla la API, intentar cargar de Room
                productoDao.obtenerTodosLosProductos().collect { productosLocales ->
                    emit(productosLocales)
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
            emit(emptyList())
        }
    }

    // Obtener productos por categoría
    fun obtenerProductosPorCategoria(categoria: String): Flow<List<ProductoEntity>> = flow {
        try {
            val productosLocales = productoDao.obtenerProductosPorCategoria(categoria)
            productosLocales.collect { emit(it) }

            try {
                val productosAPI = apiService.obtenerProductosPorCategoria(categoria)
                val productosEntity = productosAPI.map { it.toEntity() }

                productosEntity.forEach { productoDao.insertarProducto(it) }

            } catch (e: Exception) {
                Log.e("ProductoRepository", "Error al sincronizar categoría: ${e.message}")
            }
        } catch (e: Exception) {
            Log.e("ProductoRepository", "Error: ${e.message}")
        }
    }

    // Buscar productos por nombre
    fun buscarProductos(nombre: String): Flow<List<ProductoEntity>> = flow {
        try {
            val productosLocales = productoDao.buscarProductosPorNombre(nombre)
            productosLocales.collect { emit(it) }

            try {
                val productosAPI = apiService.buscarProductos(nombre)
                val productosEntity = productosAPI.map { it.toEntity() }

                productosEntity.forEach { productoDao.insertarProducto(it) }

            } catch (e: Exception) {
                Log.e("ProductoRepository", "Error en búsqueda: ${e.message}")
            }
        } catch (e: Exception) {
            Log.e("ProductoRepository", "Error: ${e.message}")
        }
    }

    // Obtener producto por ID
    suspend fun obtenerProductoPorId(id: Int): Result<ProductoEntity> {
        return try {
            val productoEntity = apiService.obtenerProductoPorId(id).toEntity()
            productoDao.insertarProducto(productoEntity)
            Result.success(productoEntity)
        } catch (e: Exception) {
            Log.e("ProductoRepository", "Error al obtener producto: ${e.message}")
            val productoLocal = productoDao.obtenerProductoPorId(id)
            if (productoLocal != null) {
                Result.success(productoLocal)
            } else {
                Result.failure(e)
            }
        }
    }
}