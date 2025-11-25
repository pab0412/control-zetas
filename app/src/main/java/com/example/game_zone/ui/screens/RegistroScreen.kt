package com.example.game_zone.ui.screens

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.material3.CardDefaults.cardColors
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.game_zone.R
import com.example.game_zone.model.GustosDisponibles
import com.example.game_zone.ui.navigation.Screen
import com.example.game_zone.viewmodel.UsuarioViewModel

// FUENTE ESTILO PIXEL
val registroFont = FontFamily(Font(R.font.pixel))

@Composable
fun RegistroScreen(
    navController: NavController,
    viewModel: UsuarioViewModel
) {
    val estado by viewModel.estado.collectAsState()
    val registroExitoso by viewModel.registroExitoso.collectAsState()
    var cargando by remember { mutableStateOf(false) }

    // Navegar cuando el registro sea exitoso
    LaunchedEffect(registroExitoso) {
        Log.d("REGISTRO_UI", "registroExitoso = $registroExitoso")
        if (registroExitoso == true) {
            Log.d("REGISTRO_UI", "✅ NAVEGANDO A LOGIN")
            cargando = false
            navController.navigate(Screen.Login.route) {
                popUpTo(Screen.Registro.route) { inclusive = true }
            }
            viewModel.resetRegistroExitoso()
            viewModel.limpiarFormulario()
        }
    }

    // Detectar errores y detener el loading
    LaunchedEffect(estado.errores) {
        val hayErrores = listOfNotNull(
            estado.errores.correo,
            estado.errores.nombre,
            estado.errores.clave,
            estado.errores.direccion,
            estado.errores.gustos
        ).isNotEmpty()

        if (hayErrores && cargando) {
            Log.d("REGISTRO_UI", "❌ Errores detectados, deteniendo loading")
            cargando = false
        }
    }

    // CONTENEDOR PRINCIPAL
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        // IMAGEN DE FONDO
        Image(
            painter = painterResource(id = R.drawable.fondo_registro),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.matchParentSize()
        )

        // CAPA NEGRA SEMITRANSPARENTE
        Box(
            modifier = Modifier
                .matchParentSize()
                .background(MaterialTheme.colorScheme.surface.copy(alpha = 0.6f))
        )

        // TARJETA PRINCIPAL (GLASS)
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            shape = RoundedCornerShape(20.dp),
            colors = cardColors(
                containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.4f)
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
        ) {
            Column(
                modifier = Modifier
                    .verticalScroll(rememberScrollState())
                    .padding(24.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {

                // ENCABEZADO
                Text(
                    text = "Crear Cuenta",
                    style = MaterialTheme.typography.headlineLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary,
                    fontFamily = registroFont
                )

                Text(
                    text = "Completa los datos para registrarte",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    fontFamily = registroFont
                )

                Spacer(modifier = Modifier.height(8.dp))

                // CAMPOS DE ENTRADA
                OutlinedTextField(
                    value = estado.nombre,
                    onValueChange = viewModel::onNombreChange,
                    label = { Text("Nombre", fontFamily = registroFont) },
                    isError = estado.errores.nombre != null,
                    supportingText = {
                        estado.errores.nombre?.let {
                            Text(text = it, color = MaterialTheme.colorScheme.error, fontFamily = registroFont)
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    enabled = !cargando
                )

                OutlinedTextField(
                    value = estado.correo,
                    onValueChange = viewModel::onCorreoChange,
                    label = { Text("Correo electrónico", fontFamily = registroFont) },
                    isError = estado.errores.correo != null,
                    supportingText = {
                        estado.errores.correo?.let {
                            Text(text = it, color = MaterialTheme.colorScheme.error, fontFamily = registroFont)
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    enabled = !cargando
                )

                OutlinedTextField(
                    value = estado.clave,
                    onValueChange = viewModel::onClaveChange,
                    label = { Text("Contraseña", fontFamily = registroFont) },
                    visualTransformation = PasswordVisualTransformation(),
                    isError = estado.errores.clave != null,
                    supportingText = {
                        estado.errores.clave?.let {
                            Text(text = it, color = MaterialTheme.colorScheme.error, fontFamily = registroFont)
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    enabled = !cargando
                )

                OutlinedTextField(
                    value = estado.direccion,
                    onValueChange = viewModel::onDireccionChange,
                    label = { Text("Dirección", fontFamily = registroFont) },
                    isError = estado.errores.direccion != null,
                    supportingText = {
                        estado.errores.direccion?.let {
                            Text(text = it, color = MaterialTheme.colorScheme.error, fontFamily = registroFont)
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    enabled = !cargando
                )

                Spacer(modifier = Modifier.height(8.dp))

                // SECCIÓN DE GUSTOS
                Text(
                    text = "Géneros Favoritos",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.primary,
                    fontFamily = registroFont
                )

                if (estado.errores.gustos != null) {
                    Text(
                        text = estado.errores.gustos!!,
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodySmall,
                        fontFamily = registroFont
                    )
                }

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)
                    ),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        GustosDisponibles.lista.forEachIndexed { index, gusto ->
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Checkbox(
                                    checked = estado.gustos.contains(gusto),
                                    onCheckedChange = { seleccionado ->
                                        viewModel.onGustoChange(gusto, seleccionado)
                                    },
                                    colors = CheckboxDefaults.colors(
                                        checkedColor = MaterialTheme.colorScheme.primary
                                    ),
                                    enabled = !cargando
                                )
                                Spacer(Modifier.width(8.dp))
                                Text(
                                    text = gusto,
                                    style = MaterialTheme.typography.bodyMedium,
                                    fontFamily = registroFont
                                )
                            }
                            if (index < GustosDisponibles.lista.size - 1) {
                                HorizontalDivider(
                                    modifier = Modifier.padding(vertical = 4.dp),
                                    color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f)
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                // TÉRMINOS Y CONDICIONES
                Row(
                    verticalAlignment = Alignment.Top,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Checkbox(
                        checked = estado.aceptaterminos,
                        onCheckedChange = viewModel::onAceptarTerminosChange,
                        colors = CheckboxDefaults.colors(
                            checkedColor = MaterialTheme.colorScheme.primary
                        ),
                        enabled = !cargando
                    )
                    Spacer(Modifier.width(8.dp))
                    Text(
                        text = "Acepto los términos y condiciones",
                        style = MaterialTheme.typography.bodyMedium,
                        fontFamily = registroFont,
                        modifier = Modifier.padding(top = 12.dp)
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                // BOTÓN DE REGISTRO
                Button(
                    onClick = {
                        Log.d("REGISTRO_UI", "🔵 Click en Registrar")
                        if (!estado.aceptaterminos) {
                            Log.d("REGISTRO_UI", "❌ Términos no aceptados")
                            return@Button
                        }
                        cargando = true
                        Log.d("REGISTRO_UI", "⏳ Cargando = true")
                        viewModel.registrarUsuario()
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    shape = RoundedCornerShape(12.dp),
                    elevation = ButtonDefaults.buttonElevation(defaultElevation = 2.dp),
                    enabled = !cargando && estado.aceptaterminos
                ) {
                    if (cargando) {
                        CircularProgressIndicator(
                            modifier = Modifier.height(24.dp),
                            color = MaterialTheme.colorScheme.onPrimary
                        )
                    } else {
                        Text(
                            text = "Registrar",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.SemiBold,
                            fontFamily = registroFont
                        )
                    }
                }

                // LINK A LOGIN
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "¿Ya tienes cuenta?",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontFamily = registroFont
                    )
                    TextButton(
                        onClick = {
                            viewModel.limpiarFormulario()
                            navController.navigate(Screen.Login.route)
                        },
                        enabled = !cargando
                    ) {
                        Text(
                            text = "Inicia sesión",
                            fontWeight = FontWeight.SemiBold,
                            fontFamily = registroFont
                        )
                    }
                }
            }
        }
    }
}