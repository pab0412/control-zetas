package com.example.game_zone.ui.screens

import com.example.game_zone.viewmodel.EstadoViewModel
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun PantallaCarga(modifier: Modifier, viewModel:EstadoViewModel = viewModel()){
    val estado = viewModel.activo.collectAsState()
    val mostrarMensaje = viewModel.mostrarMensaje.collectAsState()

    if (estado.value == null){
        Box(
            modifier = modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
    } else {
        val estadoActivo = estado.value!!
        val colorAnimado by animateColorAsState(
            targetValue = if (estadoActivo) Color.Green else Color.Red,
            animationSpec = tween(durationMillis = 500), label = ""
        )

        val textoBoton by remember(estadoActivo) {
            derivedStateOf { (if (estadoActivo) "Desactivar" else "Activar") }
        }
    }
}