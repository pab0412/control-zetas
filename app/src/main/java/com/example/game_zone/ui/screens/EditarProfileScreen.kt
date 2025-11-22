package com.example.game_zone.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.game_zone.viewmodel.UsuarioViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditarPerfilScreen(
    navController: NavController,
    viewModel: UsuarioViewModel
) {
    val estado by viewModel.estado.collectAsStateWithLifecycle()
    val cargando by viewModel.cargando.collectAsStateWithLifecycle()

    var mostrarDialogoPassword by remember { mutableStateOf(false) }
    var passwordConfirmacion by remember { mutableStateOf("") }
    var mostrarPassword by remember { mutableStateOf(false) }
    var errorPassword by remember { mutableStateOf<String?>(null) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Editar Perfil") },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Volver"
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {
            Text(
                text = "Información Personal",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Nombre
            OutlinedTextField(
                value = estado.nombre,
                onValueChange = { viewModel.onNombreChange(it) },
                label = { Text("Nombre") },
                modifier = Modifier.fillMaxWidth(),
                isError = estado.errores.nombre != null,
                supportingText = {
                    estado.errores.nombre?.let { Text(it) }
                }
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Correo (no editable por seguridad)
            OutlinedTextField(
                value = estado.correo,
                onValueChange = {},
                label = { Text("Correo") },
                modifier = Modifier.fillMaxWidth(),
                enabled = false,
                colors = OutlinedTextFieldDefaults.colors(
                    disabledTextColor = MaterialTheme.colorScheme.onSurface,
                    disabledBorderColor = MaterialTheme.colorScheme.outline,
                    disabledLabelColor = MaterialTheme.colorScheme.onSurfaceVariant
                )
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Dirección
            OutlinedTextField(
                value = estado.direccion,
                onValueChange = { viewModel.onDireccionChange(it) },
                label = { Text("Dirección") },
                modifier = Modifier.fillMaxWidth(),
                isError = estado.errores.direccion != null,
                supportingText = {
                    estado.errores.direccion?.let { Text(it) }
                }
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Gustos
            Text(
                text = "Gustos",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(8.dp))

            val gustosOpciones = listOf("Acción", "Aventura", "RPG", "Deportes", "Estrategia")

            gustosOpciones.forEach { gusto ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Checkbox(
                        checked = estado.gustos.contains(gusto),
                        onCheckedChange = { checked ->
                            viewModel.onGustoChange(gusto, checked)
                        }
                    )
                    Text(text = gusto)
                }
            }

            if (estado.errores.gustos != null) {
                Text(
                    text = estado.errores.gustos!!,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(start = 16.dp)
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Botón Guardar Cambios
            Button(
                onClick = {
                    mostrarDialogoPassword = true
                    errorPassword = null
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                enabled = !cargando
            ) {
                if (cargando) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                } else {
                    Text("Guardar Cambios", fontSize = 16.sp)
                }
            }
        }

        // Diálogo de confirmación con contraseña
        if (mostrarDialogoPassword) {
            AlertDialog(
                onDismissRequest = {
                    mostrarDialogoPassword = false
                    passwordConfirmacion = ""
                    errorPassword = null
                },
                title = {
                    Text(
                        "Confirmar cambios",
                        fontWeight = FontWeight.Bold
                    )
                },
                text = {
                    Column {
                        Text("Por seguridad, ingresa tu contraseña actual para confirmar los cambios.")

                        Spacer(modifier = Modifier.height(16.dp))

                        OutlinedTextField(
                            value = passwordConfirmacion,
                            onValueChange = {
                                passwordConfirmacion = it
                                errorPassword = null
                            },
                            label = { Text("Contraseña actual") },
                            visualTransformation = if (mostrarPassword)
                                VisualTransformation.None
                            else
                                PasswordVisualTransformation(),
                            trailingIcon = {
                                IconButton(onClick = { mostrarPassword = !mostrarPassword }) {
                                    Icon(
                                        imageVector = if (mostrarPassword)
                                            Icons.Default.Visibility
                                        else
                                            Icons.Default.VisibilityOff,
                                        contentDescription = if (mostrarPassword)
                                            "Ocultar contraseña"
                                        else
                                            "Mostrar contraseña"
                                    )
                                }
                            },
                            isError = errorPassword != null,
                            supportingText = {
                                errorPassword?.let {
                                    Text(it, color = MaterialTheme.colorScheme.error)
                                }
                            },
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                },
                confirmButton = {
                    Button(
                        onClick = {
                            // Validar que la contraseña coincida
                            if (passwordConfirmacion == estado.clave) {
                                viewModel.actualizarUsuario()
                                mostrarDialogoPassword = false
                                passwordConfirmacion = ""
                                errorPassword = null
                                navController.navigateUp()
                            } else {
                                errorPassword = "Contraseña incorrecta"
                            }
                        }
                    ) {
                        Text("Confirmar")
                    }
                },
                dismissButton = {
                    TextButton(
                        onClick = {
                            mostrarDialogoPassword = false
                            passwordConfirmacion = ""
                            errorPassword = null
                        }
                    ) {
                        Text("Cancelar")
                    }
                }
            )
        }
    }
}