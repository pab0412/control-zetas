package com.example.game_zone.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.game_zone.R
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

    // Colores estilo Dragon Ball Z
    val dbzOrange = Color(0xFFFFA500)
    val dbzGold = Color(0xFFFFD27F)
    val dbzDarkBg = Color(0xAA1A1A1A)
    val dbzRed = Color(0xFFFF5E5E)

    Box(modifier = Modifier.fillMaxSize()) {
        // FONDO DRAGON BALL Z
        Image(
            painter = painterResource(id = R.drawable.fondo_dragonball),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        // Sombra oscura para legibilidad
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0x99000000))
        )

        Scaffold(
            containerColor = Color.Transparent,
            topBar = {
                TopAppBar(
                    title = {
                        Text(
                            "EDITAR GUERRERO",
                            fontWeight = FontWeight.ExtraBold,
                            color = dbzOrange
                        )
                    },
                    navigationIcon = {
                        IconButton(
                            onClick = { navController.navigateUp() },
                            modifier = Modifier
                                .padding(8.dp)
                                .background(dbzDarkBg, CircleShape)
                        ) {
                            Icon(
                                Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Volver",
                                tint = dbzOrange
                            )
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = Color(0xCC1A1A1A)
                    )
                )
            }
        ) { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .verticalScroll(rememberScrollState())
                    .padding(20.dp)
            ) {
                Spacer(modifier = Modifier.height(16.dp))

                // Encabezado estilo DBZ
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .shadow(12.dp, RoundedCornerShape(16.dp)),
                    colors = CardDefaults.cardColors(
                        containerColor = dbzDarkBg
                    ),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(20.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = " DATOS DEL GUERRERO Z ",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = dbzOrange
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Box(
                            modifier = Modifier
                                .width(80.dp)
                                .height(3.dp)
                                .background(dbzOrange, RoundedCornerShape(2.dp))
                        )
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Campo Nombre - Estilo Cápsula
                DBZTextField(
                    value = estado.nombre,
                    onValueChange = { viewModel.onNombreChange(it) },
                    label = "⚡ NOMBRE DEL GUERRERO",
                    isError = estado.errores.nombre != null,
                    errorMessage = estado.errores.nombre
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Campo Correo (no editable) - Estilo bloqueado
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .shadow(6.dp, RoundedCornerShape(12.dp)),
                    colors = CardDefaults.cardColors(
                        containerColor = Color(0x66333333)
                    ),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "COMUNICADOR SCOUTER",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = dbzGold
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = estado.correo,
                            fontSize = 16.sp,
                            color = Color.White,
                            fontWeight = FontWeight.Medium
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Bloqueado por Kaio-sama",
                            fontSize = 11.sp,
                            color = dbzGold,
                            fontStyle = androidx.compose.ui.text.font.FontStyle.Italic
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Campo Dirección
                DBZTextField(
                    value = estado.direccion,
                    onValueChange = { viewModel.onDireccionChange(it) },
                    label = "🗺️ UBICACIÓN BASE",
                    isError = estado.errores.direccion != null,
                    errorMessage = estado.errores.direccion
                )

                Spacer(modifier = Modifier.height(28.dp))

                // Sección de Gustos - Estilo Esferas del Dragón
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .shadow(10.dp, RoundedCornerShape(16.dp)),
                    colors = CardDefaults.cardColors(
                        containerColor = dbzDarkBg
                    ),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Column(modifier = Modifier.padding(20.dp)) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(40.dp)
                                    .background(dbzOrange, CircleShape)
                                    .border(2.dp, Color(0xFFFFD700), CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Text("⭐", fontSize = 20.sp)
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Text(
                                text = "TÉCNICAS FAVORITAS",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = dbzOrange
                            )
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        val gustosOpciones = listOf("Acción", "Aventura", "RPG", "Deportes", "Estrategia")

                        gustosOpciones.forEach { gusto ->
                            DBZCheckboxItem(
                                text = gusto,
                                checked = estado.gustos.contains(gusto),
                                onCheckedChange = { checked ->
                                    viewModel.onGustoChange(gusto, checked)
                                }
                            )
                        }

                        if (estado.errores.gustos != null) {
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "${estado.errores.gustos}",
                                color = dbzRed,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Medium,
                                modifier = Modifier.padding(start = 8.dp)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(32.dp))

                // Botón Guardar - Estilo Super Saiyan
                Button(
                    onClick = {
                        mostrarDialogoPassword = true
                        errorPassword = null
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(64.dp)
                        .shadow(16.dp, RoundedCornerShape(32.dp)),
                    enabled = !cargando,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = dbzOrange,
                        contentColor = Color.Black,
                        disabledContainerColor = Color.Gray
                    ),
                    shape = RoundedCornerShape(32.dp)
                ) {
                    if (cargando) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(28.dp),
                            color = Color.White,
                            strokeWidth = 3.dp
                        )
                    } else {
                        Text(
                            "GUARDAR PODER",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.ExtraBold
                        )
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))
            }
        }

        // Diálogo de confirmación estilo DBZ
        if (mostrarDialogoPassword) {
            AlertDialog(
                onDismissRequest = {
                    mostrarDialogoPassword = false
                    passwordConfirmacion = ""
                    errorPassword = null
                },
                containerColor = Color(0xFF1A1A1A),
                shape = RoundedCornerShape(20.dp),
                title = {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            "VERIFICACIÓN SHENRON",
                            fontWeight = FontWeight.ExtraBold,
                            color = dbzOrange,
                            fontSize = 20.sp
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Box(
                            modifier = Modifier
                                .width(100.dp)
                                .height(3.dp)
                                .background(dbzOrange, RoundedCornerShape(2.dp))
                        )
                    }
                },
                text = {
                    Column {
                        Text(
                            "Por seguridad, confirma tu contraseña para actualizar tus datos de guerrero.",
                            color = Color.White,
                            fontSize = 14.sp
                        )

                        Spacer(modifier = Modifier.height(20.dp))

                        OutlinedTextField(
                            value = passwordConfirmacion,
                            onValueChange = {
                                passwordConfirmacion = it
                                errorPassword = null
                            },
                            label = { Text("⚡ Contraseña Actual", color = dbzGold) },
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
                                            "Mostrar contraseña",
                                        tint = dbzOrange
                                    )
                                }
                            },
                            isError = errorPassword != null,
                            supportingText = {
                                errorPassword?.let {
                                    Text(it, color = dbzRed)
                                }
                            },
                            modifier = Modifier.fillMaxWidth(),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = dbzOrange,
                                unfocusedBorderColor = dbzGold,
                                focusedTextColor = Color.White,
                                unfocusedTextColor = Color.White,
                                errorBorderColor = dbzRed
                            )
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
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = dbzOrange,
                            contentColor = Color.Black
                        )
                    ) {
                        Text("✓ Confirmar", fontWeight = FontWeight.Bold)
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
                        Text("Cancelar", color = Color.White)
                    }
                }
            )
        }
    }
}

// Componente personalizado TextField estilo DBZ
@Composable
fun DBZTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    isError: Boolean,
    errorMessage: String?
) {
    val dbzOrange = Color(0xFFFFA500)
    val dbzGold = Color(0xFFFFD27F)
    val dbzRed = Color(0xFFFF5E5E)

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(8.dp, RoundedCornerShape(12.dp)),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xAA1A1A1A)
        ),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = label,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = if (isError) dbzRed else dbzOrange
            )
            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = value,
                onValueChange = onValueChange,
                modifier = Modifier.fillMaxWidth(),
                isError = isError,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = dbzOrange,
                    unfocusedBorderColor = dbzGold,
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White,
                    errorBorderColor = dbzRed,
                    errorTextColor = Color.White
                ),
                shape = RoundedCornerShape(8.dp)
            )

            if (errorMessage != null) {
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "$errorMessage",
                    color = dbzRed,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}

// Componente Checkbox estilo DBZ
@Composable
fun DBZCheckboxItem(
    text: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    val dbzOrange = Color(0xFFFFA500)

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Checkbox(
            checked = checked,
            onCheckedChange = onCheckedChange,
            colors = CheckboxDefaults.colors(
                checkedColor = dbzOrange,
                uncheckedColor = Color.White,
                checkmarkColor = Color.Black
            )
        )
        Text(
            text = text,
            color = Color.White,
            fontSize = 16.sp,
            fontWeight = if (checked) FontWeight.Bold else FontWeight.Normal
        )
    }
}