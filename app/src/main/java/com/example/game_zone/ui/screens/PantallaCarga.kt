package com.example.game_zone.ui.screens

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.game_zone.R
import com.example.game_zone.viewmodel.EstadoViewModel
import kotlinx.coroutines.delay

@Composable
fun PantallaCarga(
    modifier: Modifier = Modifier,
    viewModel: EstadoViewModel = viewModel()
) {
    val estado = viewModel.activo.collectAsState()

    // Colores gamer simples
    val darkBg = Color(0xFF1A1A1A)
    val accentColor = Color(0xFF6C63FF)
    val lightAccent = Color(0xFF9D97FF)

    // Animación de escala suave (respiración)
    val infiniteTransition = rememberInfiniteTransition(label = "pulse")
    val scale by infiniteTransition.animateFloat(
        initialValue = 0.95f,
        targetValue = 1.05f,
        animationSpec = infiniteRepeatable(
            animation = tween(1500, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "scale"
    )

    // Animación de alpha (fade in/out)
    val alpha by infiniteTransition.animateFloat(
        initialValue = 0.6f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(1500, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "alpha"
    )

    // Texto animado con puntos
    var dots by remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        while (true) {
            delay(500)
            dots = when (dots) {
                "" -> "."
                "." -> ".."
                ".." -> "..."
                else -> ""
            }
        }
    }

    if (estado.value == null) {
        Box(
            modifier = modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            darkBg,
                            Color(0xFF2A2A2A),
                            darkBg
                        )
                    )
                ),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                // Logo con animación suave
                Image(
                    painter = painterResource(id = R.drawable.logo),
                    contentDescription = "Logo Game Zone",
                    modifier = Modifier
                        .size(180.dp)
                        .scale(scale)
                        .alpha(alpha),
                    contentScale = ContentScale.Fit
                )

                Spacer(modifier = Modifier.height(48.dp))

                // Indicador de carga
                CircularProgressIndicator(
                    modifier = Modifier.size(48.dp),
                    color = accentColor,
                    strokeWidth = 4.dp
                )

                Spacer(modifier = Modifier.height(24.dp))

                // Texto "Cargando..."
                Text(
                    text = "Cargando$dots",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Medium,
                    color = lightAccent
                )
            }
        }
    }
}