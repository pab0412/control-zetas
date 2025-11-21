package com.example.game_zone.ui.screens

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.game_zone.model.GustosDisponibles
import com.example.game_zone.ui.components.CameraImagePicker
import com.example.game_zone.ui.components.CircularAvatarClickable
import com.example.game_zone.viewmodel.UsuarioViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditarPerfilScreen(
    navController: NavController,
    viewModel: UsuarioViewModel
) {
    val estado by viewModel.estado.collectAsState()
    val cargando by viewModel.cargando.collectAsState()
    var showImageOptions by remember { mutableStateOf(false) }
    var showSuccessDialog by remember { mutableStateOf(false) }
    var showConfirmDialog by remember { mutableStateOf(false) }

    // Variables locales para manejar la nueva contraseña
    var nuevaClave by remember { mutableStateOf("") }
    var confirmarClave by remember { mutableStateOf("") }
    var cambiarClave by remember { mutableStateOf(false) }
    var errorClave by remember { mutableStateOf<String?>(null) }

    // Diálogo de éxito
    if (showSuccessDialog) {
        AlertDialog(
            onDismissRequest = {
                showSuccessDialog = false
                navController.popBackStack()
            },
            title = { Text("Perfil Actualizado") },
            text = { Text("Tus datos se han guardado correctamente") },
            confirmButton = {
                TextButton(onClick = {
                    showSuccessDialog = false
                    navController.popBackStack()
                }) {
                    Text("Aceptar")
                }
            }
        )
    }

    // Diálogo de confirmación
    if (showConfirmDialog) {
        AlertDialog(
            onDismissRequest = { showConfirmDialog = false },
            title = { Text("Confirmar cambios") },
            text = { Text("¿Estás seguro de que deseas guardar los cambios?") },
            confirmButton = {
                TextButton(
                    onClick = {
                        showConfirmDialog = false

                        // Validar contraseña si se va a cambiar
                        if (cambiarClave) {
                            if (nuevaClave.length < 6) {
                                errorClave = "La contraseña debe tener al menos 6 caracteres"
                                return@TextButton
                            }
                            if (nuevaClave != confirmarClave) {
                                errorClave = "Las contraseñas no coinciden"
                                return@TextButton
                            }
                            viewModel.onClaveChange(nuevaClave)
                        }

                        // Actualizar usuario
                        viewModel.actualizarUsuario()
                        showSuccessDialog = true
                    }
                ) {
                    Text("Sí, guardar")
                }
            },
            dismissButton = {
                TextButton(onClick = { showConfirmDialog = false }) {
                    Text("Cancelar")
                }
            }
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Editar Perfil",
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Volver"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Spacer(modifier = Modifier.height(16.dp))

                // Avatar editable
                CircularAvatarClickable(
                    imageUrl = estado.imagen,
                    initials = estado.nombre.take(2).uppercase().ifEmpty { "UG" },
                    onClick = { showImageOptions = true },
                    size = 100.dp
                )

                Text(
                    text = "Toca para cambiar foto",
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                // Diálogo para capturar/seleccionar imagen
                CameraImagePicker(
                    currentImageUrl = estado.imagen,
                    onImageSelected = { imagePath ->
                        viewModel.onImagenChange(imagePath)
                    },
                    showOptions = showImageOptions,
                    onDismiss = { showImageOptions = false }
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Sección: Información Personal
                Text(
                    text = "Información Personal",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = estado.nombre,
                    onValueChange = viewModel::onNombreChange,
                    label = { Text("Nombre") },
                    isError = estado.errores.nombre != null,
                    supportingText = {
                        estado.errores.nombre?.let {
                            Text(text = it, color = MaterialTheme.colorScheme.error)
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    enabled = !cargando
                )

                OutlinedTextField(
                    value = estado.correo,
                    onValueChange = viewModel::onCorreoChange,
                    label = { Text("Correo electrónico") },
                    isError = estado.errores.correo != null,
                    supportingText = {
                        estado.errores.correo?.let {
                            Text(text = it, color = MaterialTheme.colorScheme.error)
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    enabled = !cargando
                )

                OutlinedTextField(
                    value = estado.direccion,
                    onValueChange = viewModel::onDireccionChange,
                    label = { Text("Dirección") },
                    isError = estado.errores.direccion != null,
                    supportingText = {
                        estado.errores.direccion?.let {
                            Text(text = it, color = MaterialTheme.colorScheme.error)
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    enabled = !cargando
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Sección: Cambiar Contraseña
                Text(
                    text = "Seguridad",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.fillMaxWidth()
                )

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Checkbox(
                        checked = cambiarClave,
                        onCheckedChange = {
                            cambiarClave = it
                            if (!it) {
                                nuevaClave = ""
                                confirmarClave = ""
                                errorClave = null
                            }
                        },
                        colors = CheckboxDefaults.colors(
                            checkedColor = MaterialTheme.colorScheme.primary
                        ),
                        enabled = !cargando
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Cambiar contraseña",
                        style = MaterialTheme.typography.bodyMedium
                    )
                }

                if (cambiarClave) {
                    OutlinedTextField(
                        value = nuevaClave,
                        onValueChange = {
                            nuevaClave = it
                            errorClave = null
                        },
                        label = { Text("Nueva contraseña") },
                        visualTransformation = PasswordVisualTransformation(),
                        isError = errorClave != null,
                        supportingText = {
                            errorClave?.let {
                                Text(text = it, color = MaterialTheme.colorScheme.error)
                            }
                        },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        enabled = !cargando
                    )

                    OutlinedTextField(
                        value = confirmarClave,
                        onValueChange = {
                            confirmarClave = it
                            errorClave = null
                        },
                        label = { Text("Confirmar contraseña") },
                        visualTransformation = PasswordVisualTransformation(),
                        isError = errorClave != null,
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        enabled = !cargando
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Sección: Intereses
                Text(
                    text = "Mis Intereses",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.fillMaxWidth()
                )

                if (estado.errores.gustos != null) {
                    Text(
                        text = estado.errores.gustos!!,
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
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
                                    style = MaterialTheme.typography.bodyMedium
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

                Spacer(modifier = Modifier.height(16.dp))

                // Botones de acción
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    // Botón Cancelar
                    OutlinedButton(
                        onClick = { navController.popBackStack() },
                        modifier = Modifier
                            .weight(1f)
                            .height(50.dp),
                        shape = RoundedCornerShape(12.dp),
                        enabled = !cargando
                    ) {
                        Text("Cancelar")
                    }

                    // Botón Guardar
                    Button(
                        onClick = { showConfirmDialog = true },
                        modifier = Modifier
                            .weight(1f)
                            .height(50.dp),
                        shape = RoundedCornerShape(12.dp),
                        enabled = !cargando
                    ) {
                        if (cargando) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(24.dp),
                                color = MaterialTheme.colorScheme.onPrimary
                            )
                        } else {
                            Text(
                                text = "Guardar",
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(32.dp))
            }
        }
    }
}