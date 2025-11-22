package com.example.game_zone

import com.example.game_zone.data.entity.UsuarioEntity
import com.example.game_zone.data.repository.UsuarioRepository
import com.example.game_zone.viewmodel.UsuarioViewModel
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import io.mockk.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.*

@OptIn(ExperimentalCoroutinesApi::class)
class UsuarioViewModelTest : DescribeSpec({

    val testDispatcher = UnconfinedTestDispatcher()

    beforeTest {
        Dispatchers.setMain(testDispatcher)
    }

    afterTest {
        Dispatchers.resetMain()
        clearAllMocks()
    }

    describe("UsuarioViewModel") {

        val mockRepository = mockk<UsuarioRepository>()

        // Datos de prueba
        val usuarioEjemplo = UsuarioEntity(
            id = 1,
            nombre = "Juan Pérez",
            correo = "juan@gmail.com",
            clave = "123456",
            direccion = "Calle Falsa 123",
            gustos = "Acción,Aventura",
            imagen = "",
            aceptaterminos = true
        )

        describe("Cambios de estado") {

            it("debe actualizar el nombre correctamente") {
                val viewModel = UsuarioViewModel(mockRepository)

                viewModel.onNombreChange("Carlos")

                viewModel.estado.value.nombre shouldBe "Carlos"
            }

            it("debe actualizar el correo correctamente") {
                val viewModel = UsuarioViewModel(mockRepository)

                viewModel.onCorreoChange("nuevo@correo.com")

                viewModel.estado.value.correo shouldBe "nuevo@correo.com"
            }

            it("debe actualizar la clave correctamente") {
                val viewModel = UsuarioViewModel(mockRepository)

                viewModel.onClaveChange("nueva123")

                viewModel.estado.value.clave shouldBe "nueva123"
            }

            it("debe actualizar la dirección correctamente") {
                val viewModel = UsuarioViewModel(mockRepository)

                viewModel.onDireccionChange("Nueva dirección 456")

                viewModel.estado.value.direccion shouldBe "Nueva dirección 456"
            }

            it("debe actualizar aceptar términos correctamente") {
                val viewModel = UsuarioViewModel(mockRepository)

                viewModel.onAceptarTerminosChange(true)

                viewModel.estado.value.aceptaterminos shouldBe true
            }

            it("debe agregar y quitar gustos correctamente") {
                val viewModel = UsuarioViewModel(mockRepository)

                viewModel.onGustoChange("Deportes", true)
                viewModel.estado.value.gustos shouldBe setOf("Deportes")

                viewModel.onGustoChange("RPG", true)
                viewModel.estado.value.gustos shouldBe setOf("Deportes", "RPG")

                viewModel.onGustoChange("Deportes", false)
                viewModel.estado.value.gustos shouldBe setOf("RPG")
            }
        }

        describe("Validación de formulario") {

            it("debe validar correctamente un formulario completo") {
                val viewModel = UsuarioViewModel(mockRepository)

                viewModel.onNombreChange("Juan")
                viewModel.onCorreoChange("juan@correo.com")
                viewModel.onClaveChange("123456")
                viewModel.onDireccionChange("Calle 123")
                viewModel.onGustoChange("Acción", true)

                val esValido = viewModel.validarFormulario()

                esValido shouldBe true
                viewModel.estado.value.errores.nombre shouldBe null
                viewModel.estado.value.errores.correo shouldBe null
                viewModel.estado.value.errores.clave shouldBe null
            }

            it("debe detectar nombre vacío") {
                val viewModel = UsuarioViewModel(mockRepository)

                viewModel.onNombreChange("")
                viewModel.onCorreoChange("juan@correo.com")
                viewModel.onClaveChange("123456")
                viewModel.onDireccionChange("Calle 123")
                viewModel.onGustoChange("Acción", true)

                val esValido = viewModel.validarFormulario()

                esValido shouldBe false
                viewModel.estado.value.errores.nombre shouldBe "Campo Obligatorio"
            }

            it("debe detectar correo inválido") {
                val viewModel = UsuarioViewModel(mockRepository)

                viewModel.onNombreChange("Juan")
                viewModel.onCorreoChange("correosinvalido")
                viewModel.onClaveChange("123456")
                viewModel.onDireccionChange("Calle 123")
                viewModel.onGustoChange("Acción", true)

                val esValido = viewModel.validarFormulario()

                esValido shouldBe false
                viewModel.estado.value.errores.correo shouldBe "Correo Invalido"
            }

            it("debe detectar clave muy corta") {
                val viewModel = UsuarioViewModel(mockRepository)

                viewModel.onNombreChange("Juan")
                viewModel.onCorreoChange("juan@correo.com")
                viewModel.onClaveChange("123")
                viewModel.onDireccionChange("Calle 123")
                viewModel.onGustoChange("Acción", true)

                val esValido = viewModel.validarFormulario()

                esValido shouldBe false
                viewModel.estado.value.errores.clave shouldBe "Clave debe tener al menos 6 digitos"
            }

            it("debe detectar cuando no hay gustos seleccionados") {
                val viewModel = UsuarioViewModel(mockRepository)

                viewModel.onNombreChange("Juan")
                viewModel.onCorreoChange("juan@correo.com")
                viewModel.onClaveChange("123456")
                viewModel.onDireccionChange("Calle 123")

                val esValido = viewModel.validarFormulario()

                esValido shouldBe false
                viewModel.estado.value.errores.gustos shouldBe "Seleccione al menos un gusto"
            }
        }

        describe("Registro de usuario") {

            it("debe registrar usuario exitosamente") {
                // Given
                coEvery { mockRepository.existeCorreo(any()) } returns false
                coEvery { mockRepository.registrarUsuario(any()) } returns Result.success(usuarioEjemplo)

                val viewModel = UsuarioViewModel(mockRepository)

                // When
                viewModel.onNombreChange("Juan Pérez")
                viewModel.onCorreoChange("juan@gmail.com")
                viewModel.onClaveChange("123456")
                viewModel.onDireccionChange("Calle Falsa 123")
                viewModel.onGustoChange("Acción", true)

                viewModel.registrarUsuario()

                // Then
                viewModel.registroExitoso.value shouldBe true
                viewModel.usuarioActualId.value shouldBe 1

                coVerify { mockRepository.registrarUsuario(any()) }
            }

            it("debe fallar cuando el correo ya existe") {
                // Given
                coEvery { mockRepository.existeCorreo("juan@gmail.com") } returns true

                val viewModel = UsuarioViewModel(mockRepository)

                // When
                viewModel.onNombreChange("Juan Pérez")
                viewModel.onCorreoChange("juan@gmail.com")
                viewModel.onClaveChange("123456")
                viewModel.onDireccionChange("Calle Falsa 123")
                viewModel.onGustoChange("Acción", true)

                viewModel.registrarUsuario()

                // Then
                viewModel.registroExitoso.value shouldBe false
                viewModel.estado.value.errores.correo shouldBe "Este correo ya está registrado"

                coVerify(exactly = 0) { mockRepository.registrarUsuario(any()) }
            }

            it("debe fallar cuando el formulario es inválido") {
                // Given
                val viewModel = UsuarioViewModel(mockRepository)

                // When - Formulario vacío
                viewModel.registrarUsuario()

                // Then
                viewModel.registroExitoso.value shouldBe false

                coVerify(exactly = 0) { mockRepository.registrarUsuario(any()) }
            }
        }

        describe("Login de usuario") {

            it("debe hacer login exitosamente") {
                // Given
                coEvery { mockRepository.loginConAPI("juan@gmail.com", "123456") } returns Result.success(usuarioEjemplo)

                val viewModel = UsuarioViewModel(mockRepository)

                // When
                viewModel.login("juan@gmail.com", "123456")

                // Then
                viewModel.loginExitoso.value shouldBe true
                viewModel.usuarioActualId.value shouldBe 1
                viewModel.estado.value.nombre shouldBe "Juan Pérez"
                viewModel.estado.value.correo shouldBe "juan@gmail.com"

                coVerify { mockRepository.loginConAPI("juan@gmail.com", "123456") }
            }

            it("debe fallar login con credenciales incorrectas") {
                // Given
                coEvery { mockRepository.loginConAPI("juan@gmail.com", "incorrecta") } returns
                        Result.failure(Exception("Credenciales incorrectas"))

                val viewModel = UsuarioViewModel(mockRepository)

                // When
                viewModel.login("juan@gmail.com", "incorrecta")

                // Then
                viewModel.loginExitoso.value shouldBe false
                viewModel.estado.value.errores.correo shouldNotBe null
            }
        }

        describe("Cargar usuario") {

            it("debe manejar cuando el usuario no existe") {
                runTest {
                    // Given
                    coEvery { mockRepository.obtenerUsuarioPorId(999) } returns null
                    coEvery { mockRepository.sincronizarUsuario(999) } returns Result.failure(Exception("Usuario no encontrado"))

                    val viewModel = UsuarioViewModel(mockRepository)

                    // When
                    viewModel.cargarUsuario(999)
                    advanceUntilIdle() // Esperar a que termine la coroutine

                    // Then
                    // Como no existe, el estado no debería cambiar
                    viewModel.estado.value.nombre shouldBe ""

                    coVerify { mockRepository.obtenerUsuarioPorId(999) }
                }
            }
        }

        describe("Cerrar sesión") {

            it("debe limpiar datos al cerrar sesión") {
                // Given
                coEvery { mockRepository.loginConAPI(any(), any()) } returns Result.success(usuarioEjemplo)

                val viewModel = UsuarioViewModel(mockRepository)
                viewModel.login("juan@gmail.com", "123456")

                // When
                viewModel.cerrarSesion()

                // Then
                viewModel.usuarioActualId.value shouldBe null
                viewModel.loginExitoso.value shouldBe false
                viewModel.estado.value.nombre shouldBe ""
                viewModel.estado.value.correo shouldBe ""
            }
        }

        describe("Limpiar formulario") {

            it("debe resetear todo el formulario") {
                // Given
                val viewModel = UsuarioViewModel(mockRepository)
                viewModel.onNombreChange("Juan")
                viewModel.onCorreoChange("juan@correo.com")

                // When
                viewModel.limpiarFormulario()

                // Then
                viewModel.estado.value.nombre shouldBe ""
                viewModel.estado.value.correo shouldBe ""
                viewModel.registroExitoso.value shouldBe false
            }
        }
    }
})