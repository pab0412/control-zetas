package com.example.game_zone.data.dao

import androidx.room.*
import com.example.game_zone.data.entity.ProductoEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ProductoDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertarProducto(producto: ProductoEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertarProductos(productos: List<ProductoEntity>)

    @Update
    suspend fun actualizarProducto(producto: ProductoEntity)

    @Delete
    suspend fun eliminarProducto(producto: ProductoEntity)

    @Query("DELETE FROM productos")
    suspend fun eliminarTodos()

    @Query("SELECT * FROM productos WHERE id = :id")
    suspend fun obtenerProductoPorId(id: Int): ProductoEntity?

    @Query("SELECT * FROM productos")
    fun obtenerTodosLosProductos(): Flow<List<ProductoEntity>>

    @Query("SELECT * FROM productos WHERE categoria = :categoria")
    fun obtenerProductosPorCategoria(categoria: String): Flow<List<ProductoEntity>>

    @Query("SELECT * FROM productos WHERE nombre LIKE '%' || :nombre || '%'")
    fun buscarProductosPorNombre(nombre: String): Flow<List<ProductoEntity>>
}
