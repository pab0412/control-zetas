package com.example.game_zone.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.game_zone.data.entity.ProductoEntity
import com.example.game_zone.data.repository.ProductoRepository
import com.example.game_zone.model.ProductoUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ProductoViewModel(
    private val repository: ProductoRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ProductoUiState())
    val uiState: StateFlow<ProductoUiState> = _uiState.asStateFlow()

    init {
        obtenerTodosLosProductos()
    }

    fun obtenerTodosLosProductos() {
        viewModelScope.launch {
            _uiState.update { it.copy(cargando = true) }
            try {
                repository.obtenerTodosLosProductos().collect { productos ->
                    _uiState.update {
                        it.copy(
                            productos = productos,
                            cargando = false,
                            error = null
                        )
                    }
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        cargando = false,
                        error = "Error al cargar productos: ${e.message}"
                    )
                }
            }
        }
    }

    fun filtrarPorCategoria(categoria: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(cargando = true, categoriaPorFiltrar = categoria) }
            try {
                repository.obtenerProductosPorCategoria(categoria).collect { productos ->
                    _uiState.update {
                        it.copy(
                            productos = productos,
                            cargando = false,
                            error = null
                        )
                    }
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        cargando = false,
                        error = "Error al filtrar: ${e.message}"
                    )
                }
            }
        }
    }

    fun buscarProductos(nombre: String) {
        _uiState.update { it.copy(busqueda = nombre) }
        viewModelScope.launch {
            _uiState.update { it.copy(cargando = true) }
            try {
                repository.buscarProductos(nombre).collect { productos ->
                    _uiState.update {
                        it.copy(
                            productos = productos,
                            cargando = false,
                            error = null
                        )
                    }
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        cargando = false,
                        error = "Error en búsqueda: ${e.message}"
                    )
                }
            }
        }
    }

    fun seleccionarProducto(producto: ProductoEntity) {
        _uiState.update { it.copy(productoSeleccionado = producto) }
    }

    fun limpiarSeleccion() {
        _uiState.update { it.copy(productoSeleccionado = null) }
    }

    class ProductoViewModelFactory(
        private val repository: ProductoRepository
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(ProductoViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return ProductoViewModel(repository) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}
