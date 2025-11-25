package com.example.game_zone.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.game_zone.R
import com.example.game_zone.ui.navigation.Screen
import com.example.game_zone.viewmodel.MainViewModel

@Composable
fun SettingsScreen(
    navController: NavController,
    viewModel: MainViewModel = viewModel()
) {
    // Paleta de colores moderna
    val primaryColor = Color(0xFF6366F1)
    val secondaryColor = Color(0xFF8B5CF6)
    val accentColor = Color(0xFFEC4899)

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        // Fondo con imagen
        Image(
            painter = painterResource(id = R.drawable.fondomovil),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize(),
            alpha = 0.4f
        )

        // Capa de gradiente superpuesta
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color.Black.copy(alpha = 0.7f),
                            Color.Black.copy(alpha = 0.5f)
                        )
                    )
                )
        )

        // Contenido principal
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(24.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            // Icono principal
            Icon(
                imageVector = Icons.Filled.Settings,
                contentDescription = "Settings",
                modifier = Modifier.size(80.dp),
                tint = Color.White
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Título
            Text(
                text = "Configuración",
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )

            Text(
                text = "Gestiona tus preferencias",
                fontSize = 16.sp,
                color = Color.White.copy(alpha = 0.7f)
            )

            Spacer(modifier = Modifier.height(48.dp))

            // Tarjeta con opciones
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color.White.copy(alpha = 0.1f)
                ),
                elevation = CardDefaults.cardElevation(8.dp)
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {

                    // Botón Volver al Inicio
                    Button(
                        onClick = {
                            viewModel.navigateTo(Screen.Home)
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.Transparent
                        ),
                        border = ButtonDefaults.outlinedButtonBorder.copy(
                            width = 2.dp,
                            brush = Brush.horizontalGradient(
                                colors = listOf(primaryColor, secondaryColor)
                            )
                        ),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Home,
                            contentDescription = null,
                            modifier = Modifier.size(24.dp),
                            tint = Color.White
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(
                            text = "Volver al Inicio",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }

                    // Botón Ir a Perfil
                    Button(
                        onClick = {
                            viewModel.navigateTo(Screen.Profile)
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.Transparent
                        ),
                        border = ButtonDefaults.outlinedButtonBorder.copy(
                            width = 2.dp,
                            brush = Brush.horizontalGradient(
                                colors = listOf(secondaryColor, accentColor)
                            )
                        ),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Person,
                            contentDescription = null,
                            modifier = Modifier.size(24.dp),
                            tint = Color.White
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(
                            text = "Ir a Perfil",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Información adicional
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color.White.copy(alpha = 0.05f)
                )
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    SettingItem("Versión", "1.0.0")
                    HorizontalDivider(color = Color.White.copy(alpha = 0.2f))
                    SettingItem("Tema", "Oscuro")
                    HorizontalDivider(color = Color.White.copy(alpha = 0.2f))
                    SettingItem("Idioma", "Español")
                }
            }
        }
    }
}

@Composable
fun SettingItem(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            fontSize = 15.sp,
            color = Color.White.copy(alpha = 0.8f)
        )
        Text(
            text = value,
            fontSize = 15.sp,
            fontWeight = FontWeight.SemiBold,
            color = Color.White
        )
    }
}