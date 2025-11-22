package com.example.game_zone

import com.example.game_zone.data.entity.ProductoEntity
import com.example.game_zone.data.repository.ProductoRepository
import com.example.game_zone.viewmodel.ProductoViewModel
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import io.mockk.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain

@OptIn(ExperimentalCoroutinesApi::class)
class ProductoViewModelTest : DescribeSpec({

    val testDispatcher = UnconfinedTestDispatcher()

    // Setup y teardown
    beforeTest {
        Dispatchers.setMain(testDispatcher)
    }

    afterTest {
        Dispatchers.resetMain()
        clearAllMocks()
    }

    describe("ProductoViewModel") {

        val mockRepository = mockk<ProductoRepository>()

        // Datos de prueba
        val productosEjemplo = listOf(
            ProductoEntity(
                id = 1,
                nombre = "PlayStation 5",
                precio = 499.99,
                descripcion = "Consola next-gen",
                categoria = "Electrónica"
            ),
            ProductoEntity(
                id = 2,
                nombre = "Control DualSense",
                precio = 69.99,
                descripcion = "Control PS5",
                categoria = "Accesorios"
            ),
            ProductoEntity(
                id = 3,
                nombre = "Xbox Series X",
                precio = 449.99,
                descripcion = "Consola Microsoft",
                categoria = "Electrónica"
            )
        )

        describe("obtenerTodosLosProductos") {

            it("debe cargar productos exitosamente") {
                // Given
                every { mockRepository.obtenerTodosLosProductos() } returns flowOf(productosEjemplo)

                val viewModel = ProductoViewModel(mockRepository)

                // When - El init ya llama a obtenerTodosLosProductos()
                println("Productos: ${viewModel.uiState.value.productos.size}")
                println(" Cargando: ${viewModel.uiState.value.cargando}")
                println("Error: ${viewModel.uiState.value.error}")

                // Then
                viewModel.uiState.value.productos.size shouldBe 3
                viewModel.uiState.value.cargando shouldBe false
                viewModel.uiState.value.error shouldBe null

                verify { mockRepository.obtenerTodosLosProductos() }
            }

            it("debe manejar error al cargar productos") {
                // Given
                every { mockRepository.obtenerTodosLosProductos() } throws Exception("Error de red")

                val viewModel = ProductoViewModel(mockRepository)

                // Then
                viewModel.uiState.value.productos.size shouldBe 0
                viewModel.uiState.value.cargando shouldBe false
                viewModel.uiState.value.error shouldBe "Error al cargar productos: Error de red"
            }

            it("debe actualizar estado de carga correctamente") {
                // Given
                every { mockRepository.obtenerTodosLosProductos() } returns flowOf(productosEjemplo)

                val viewModel = ProductoViewModel(mockRepository)

                // Then - Debe terminar con cargando = false
                viewModel.uiState.value.cargando shouldBe false
            }
        }

        describe("filtrarPorCategoria") {

            it("debe filtrar productos por categoría Electrónica") {
                // Given
                val productosElectronica = productosEjemplo.filter { it.categoria == "Electrónica" }
                every { mockRepository.obtenerTodosLosProductos() } returns flowOf(productosEjemplo)
                every { mockRepository.obtenerProductosPorCategoria("Electrónica") } returns flowOf(productosElectronica)

                val viewModel = ProductoViewModel(mockRepository)

                // When
                viewModel.filtrarPorCategoria("Electrónica")

                // Then
                viewModel.uiState.value.productos.size shouldBe 2
                viewModel.uiState.value.productos.all { it.categoria == "Electrónica" } shouldBe true
                viewModel.uiState.value.categoriaPorFiltrar shouldBe "Electrónica"

                verify { mockRepository.obtenerProductosPorCategoria("Electrónica") }
            }

            it("debe filtrar productos por categoría Accesorios") {
                // Given
                val productosAccesorios = productosEjemplo.filter { it.categoria == "Accesorios" }
                every { mockRepository.obtenerTodosLosProductos() } returns flowOf(productosEjemplo)
                every { mockRepository.obtenerProductosPorCategoria("Accesorios") } returns flowOf(productosAccesorios)

                val viewModel = ProductoViewModel(mockRepository)

                // When
                viewModel.filtrarPorCategoria("Accesorios")

                // Then
                viewModel.uiState.value.productos.size shouldBe 1
                viewModel.uiState.value.productos[0].categoria shouldBe "Accesorios"
            }
        }

        describe("buscarProductos") {

            it("debe buscar productos por nombre") {
                // Given
                val resultadoBusqueda = listOf(productosEjemplo[0])
                every { mockRepository.obtenerTodosLosProductos() } returns flowOf(productosEjemplo)
                every { mockRepository.buscarProductos("PlayStation") } returns flowOf(resultadoBusqueda)

                val viewModel = ProductoViewModel(mockRepository)

                // When
                viewModel.buscarProductos("PlayStation")

                // Then
                viewModel.uiState.value.productos.size shouldBe 1
                viewModel.uiState.value.productos[0].nombre shouldBe "PlayStation 5"
                viewModel.uiState.value.busqueda shouldBe "PlayStation"

                verify { mockRepository.buscarProductos("PlayStation") }
            }

            it("debe retornar lista vacía cuando no encuentra productos") {
                // Given
                every { mockRepository.obtenerTodosLosProductos() } returns flowOf(productosEjemplo)
                every { mockRepository.buscarProductos("NoExiste") } returns flowOf(emptyList())

                val viewModel = ProductoViewModel(mockRepository)

                // When
                viewModel.buscarProductos("NoExiste")

                // Then
                viewModel.uiState.value.productos.size shouldBe 0
            }
        }

        describe("seleccionarProducto") {

            it("debe seleccionar un producto correctamente") {
                // Given
                every { mockRepository.obtenerTodosLosProductos() } returns flowOf(productosEjemplo)
                val viewModel = ProductoViewModel(mockRepository)
                val productoASeleccionar = productosEjemplo[0]

                // When
                viewModel.seleccionarProducto(productoASeleccionar)

                // Then
                viewModel.uiState.value.productoSeleccionado shouldBe productoASeleccionar
            }
        }

        describe("limpiarSeleccion") {

            it("debe limpiar la selección de producto") {
                // Given
                every { mockRepository.obtenerTodosLosProductos() } returns flowOf(productosEjemplo)
                val viewModel = ProductoViewModel(mockRepository)
                viewModel.seleccionarProducto(productosEjemplo[0])

                // When
                viewModel.limpiarSeleccion()

                // Then
                viewModel.uiState.value.productoSeleccionado shouldBe null
            }
        }
    }
})