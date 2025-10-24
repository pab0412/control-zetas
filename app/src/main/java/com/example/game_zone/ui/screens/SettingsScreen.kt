package com.example.game_zone.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.game_zone.ui.navegation.Screen
import com.example.game_zone.view_model.MainViewModel

@Composable
fun SettingsScreen(
    navController: NavController,                // Controlador de navegación para moverse entre pantallas
    viewModel: MainViewModel = viewModel()       // ViewModel que centraliza la navegación (eventos)
) {
    //  Estructura visual centralizada
    Column(
        modifier = Modifier
            .fillMaxSize()                       // Ocupa todo el alto disponible
            .padding(all = 16.dp),               // Margen interno general
        verticalArrangement = Arrangement.Center, // Centrar elementos verticalmente
        horizontalAlignment = Alignment.CenterHorizontally // Centrar elementos horizontalmente
    ) {
        // 🏷️ Título o texto principal
        Text(text = "Pantalla de Configuración (Settings)")

        Spacer(modifier = Modifier.height(24.dp)) // Espacio vertical

        //  Botón para volver al Home
        Button(
            onClick = {
                viewModel.navigateTo(Screen.Home) // Emitir evento de navegación al ViewModel
            }
        ) {
            Text(text = "Volver al Inicio")
        }

        Spacer(modifier = Modifier.height(16.dp)) // Más espacio

        // Botón para ir al Perfil
        Button(
            onClick = {
                viewModel.navigateTo(Screen.Profile) // Emitir evento para ir a perfil
            }
        ) {
            Text(text = "Ir a Perfil")
        }
    }
}
