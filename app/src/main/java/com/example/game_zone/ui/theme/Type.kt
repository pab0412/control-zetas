package com.example.game_zone.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
val Typography = Typography(
    // Estilo para el texto general del cuerpo
    bodyLarge = TextStyle(
        fontFamily = FontFamily.Default, // <--- ¡Asegúrate que sea Default!
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp
    ),

    // Estilo para los títulos grandes
    headlineLarge = TextStyle(
        fontFamily = FontFamily.Default, // <--- ¡Asegúrate que sea Default!
        fontWeight = FontWeight.Bold,
        fontSize = 32.sp,
        lineHeight = 40.sp,
        letterSpacing = 0.sp
    ),

    // Estilo para los subtítulos
    titleMedium = TextStyle(
        fontFamily = FontFamily.Default, // <--- ¡Asegúrate que sea Default!
        fontWeight = FontWeight.SemiBold,
        fontSize = 18.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp
    )

    // Agrega aquí cualquier otro estilo que tenías antes
)