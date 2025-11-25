package com.example.game_zone.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.game_zone.R
import com.example.game_zone.ui.navigation.Screen
import com.example.game_zone.viewmodel.UsuarioViewModel

@Composable
fun LoginScreen(
    navController: NavController,
    viewModel: UsuarioViewModel
) {
    val loginExitoso by viewModel.loginExitoso.collectAsState()
    val estado by viewModel.estado.collectAsState()
    val cargando by viewModel.cargando.collectAsState()

    var correo by remember { mutableStateOf("") }
    var clave by remember { mutableStateOf("") }

    LaunchedEffect(loginExitoso) {
        if (loginExitoso) {
            navController.navigate(Screen.Home.route) {
                popUpTo(Screen.Login.route) { inclusive = true }
            }
            viewModel.resetLoginExitoso()
        }
    }

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        // 👉 Fondo Dragon Ball igual que Profile
        Image(
            painter = painterResource(id = R.drawable.fondomovil),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        // 👉 Capa transparente suave tipo glow
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White.copy(alpha = 0.05f))
        )

        // 👉 Contenido scroll
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp, vertical = 48.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            // ⭐ TARJETA GLASS LOGIN
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = Color.White.copy(alpha = 0.15f)
                ),
                shape = RoundedCornerShape(22.dp),
                elevation = CardDefaults.cardElevation(6.dp)
            ) {
                Column(
                    modifier = Modifier.padding(28.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(18.dp)
                ) {

                    // ⭐ TÍTULO FUTURISTA
                    Text(
                        text = "Bienvenido",
                        fontSize = 30.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )

                    Text(
                        text = "Inicia sesión para continuar",
                        fontSize = 15.sp,
                        color = Color.White.copy(alpha = 0.8f)
                    )

                    // ⭐ Campo Correo (Glass Input)
                    OutlinedTextField(
                        value = correo,
                        onValueChange = {
                            correo = it
                            if (estado.errores.correo != null) viewModel.onCorreoChange(it)
                        },
                        label = { Text("Correo electrónico", color = Color.White) },
                        isError = estado.errores.correo != null,
                        supportingText = {
                            estado.errores.correo?.let {
                                Text(text = it, color = Color.Red)
                            }
                        },
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color.White,
                            unfocusedBorderColor = Color.White.copy(alpha = 0.5f),
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White,
                            cursorColor = Color.White
                        ),
                        textStyle = LocalTextStyle.current.copy(color = Color.White),
                        shape = RoundedCornerShape(16.dp),
                        enabled = !cargando
                    )

                    // ⭐ Campo Contraseña (Glass Input)
                    OutlinedTextField(
                        value = clave,
                        onValueChange = {
                            clave = it
                            if (estado.errores.clave != null) viewModel.onClaveChange(it)
                        },
                        label = { Text("Contraseña", color = Color.White) },
                        visualTransformation = PasswordVisualTransformation(),
                        isError = estado.errores.clave != null,
                        supportingText = {
                            estado.errores.clave?.let {
                                Text(text = it, color = Color.Red)
                            }
                        },
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color.White,
                            unfocusedBorderColor = Color.White.copy(alpha = 0.5f),
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White,
                            cursorColor = Color.White
                        ),
                        textStyle = LocalTextStyle.current.copy(color = Color.White),
                        shape = RoundedCornerShape(16.dp),
                        enabled = !cargando
                    )

                    // ⭐ BOTÓN NEÓN FUTURISTA
                    Button(
                        onClick = {
                            if (correo.isNotBlank() && clave.isNotBlank()) {
                                viewModel.limpiarErrores()
                                viewModel.login(correo, clave)
                            } else {
                                if (correo.isBlank()) viewModel.onCorreoChange("")
                                if (clave.isBlank()) viewModel.onClaveChange("")
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.Transparent
                        ),
                        border = ButtonDefaults.outlinedButtonBorder.copy(
                            width = 2.dp,
                            brush = Brush.linearGradient(
                                colors = listOf(Color.Cyan, Color.Magenta)
                            )
                        ),
                        shape = RoundedCornerShape(16.dp),
                        enabled = !cargando
                    ) {
                        if (cargando) {
                            CircularProgressIndicator(
                                modifier = Modifier.height(24.dp),
                                color = Color.White
                            )
                        } else {
                            Text(
                                text = "Iniciar Sesión",
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        }
                    }

                    // ⭐ LINK REGISTRO
                    Row(
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "¿No tienes cuenta?",
                            color = Color.White.copy(alpha = 0.8f)
                        )
                        TextButton(
                            onClick = {
                                viewModel.limpiarFormulario()
                                navController.navigate(Screen.Registro.route)
                            },
                            enabled = !cargando
                        ) {
                            Text(
                                text = "Regístrate",
                                color = Color.Cyan,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
        }
    }
}
