package com.example.game_zone.model

import com.example.game_zone.data.entity.ProductoEntity

data class ProductoUiState(
    val productos: List<ProductoEntity> = emptyList(),
    val productoSeleccionado: ProductoEntity? = null,
    val cargando: Boolean = false,
    val error: String? = null,
    val busqueda: String = "",
    val categoriaPorFiltrar: String? = null
)