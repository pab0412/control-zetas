package com.example.game_zone.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.compose.AsyncImagePainter
import coil.request.ImageRequest
import java.io.File

// Componente básico con icono por defecto
@Composable
fun CircularAvatar(
    imageUrl: String?,
    modifier: Modifier = Modifier,
    size: Dp = 100.dp,
    defaultIcon: ImageVector = Icons.Filled.Person,
    backgroundColor: Color = MaterialTheme.colorScheme.primaryContainer,
    iconTint: Color = MaterialTheme.colorScheme.onPrimaryContainer,
    contentDescription: String = "Avatar"
) {
    var isError by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(true) }

    Box(
        modifier = modifier
            .size(size)
            .clip(CircleShape)
            .background(backgroundColor),
        contentAlignment = Alignment.Center
    ) {
        if (imageUrl.isNullOrBlank() || isError) {
            // Mostrar icono por defecto
            Icon(
                imageVector = defaultIcon,
                contentDescription = contentDescription,
                modifier = Modifier.size(size * 0.6f),
                tint = iconTint
            )
        } else {
            // Intentar cargar la imagen
            val imageModel = remember(imageUrl) {
                if (imageUrl.startsWith("http")) {
                    // Es una URL
                    imageUrl
                } else {
                    // Es una ruta local
                    File(imageUrl)
                }
            }

            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(imageModel)
                    .crossfade(true)
                    .build(),
                contentDescription = contentDescription,
                modifier = Modifier
                    .fillMaxSize()
                    .clip(CircleShape),
                contentScale = ContentScale.Crop,
                onState = { state ->
                    when (state) {
                        is AsyncImagePainter.State.Loading -> {
                            isLoading = true
                            isError = false
                        }
                        is AsyncImagePainter.State.Success -> {
                            isLoading = false
                            isError = false
                        }
                        is AsyncImagePainter.State.Error -> {
                            isLoading = false
                            isError = true
                        }
                        else -> {}
                    }
                }
            )

            // Mostrar placeholder mientras carga
            if (isLoading) {
                Icon(
                    imageVector = defaultIcon,
                    contentDescription = "Cargando...",
                    modifier = Modifier.size(size * 0.6f),
                    tint = iconTint.copy(alpha = 0.5f)
                )
            }
        }
    }
}

// Componente con iniciales por defecto
@Composable
fun CircularAvatarWithInitials(
    imageUrl: String?,
    initials: String,
    modifier: Modifier = Modifier,
    size: Dp = 100.dp,
    backgroundColor: Color = MaterialTheme.colorScheme.primaryContainer,
    textColor: Color = MaterialTheme.colorScheme.onPrimaryContainer,
    contentDescription: String = "Avatar"
) {
    var isError by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(true) }

    Box(
        modifier = modifier
            .size(size)
            .clip(CircleShape)
            .background(backgroundColor),
        contentAlignment = Alignment.Center
    ) {
        if (imageUrl.isNullOrBlank() || isError) {
            // Mostrar iniciales por defecto
            Text(
                text = initials.take(2).uppercase(),
                style = MaterialTheme.typography.headlineMedium,
                color = textColor
            )
        } else {
            // Intentar cargar la imagen
            val imageModel = remember(imageUrl) {
                if (imageUrl.startsWith("http")) {
                    // Es una URL
                    imageUrl
                } else {
                    // Es una ruta local
                    File(imageUrl)
                }
            }

            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(imageModel)
                    .crossfade(true)
                    .build(),
                contentDescription = contentDescription,
                modifier = Modifier
                    .fillMaxSize()
                    .clip(CircleShape),
                contentScale = ContentScale.Crop,
                onState = { state ->
                    when (state) {
                        is AsyncImagePainter.State.Loading -> {
                            isLoading = true
                            isError = false
                        }
                        is AsyncImagePainter.State.Success -> {
                            isLoading = false
                            isError = false
                        }
                        is AsyncImagePainter.State.Error -> {
                            isLoading = false
                            isError = true
                        }
                        else -> {}
                    }
                }
            )

            // Mostrar iniciales mientras carga
            if (isLoading) {
                Text(
                    text = initials.take(2).uppercase(),
                    style = MaterialTheme.typography.headlineMedium,
                    color = textColor.copy(alpha = 0.5f)
                )
            }
        }
    }
}

// Componente clickeable con badge de cámara
@Composable
fun CircularAvatarClickable(
    imageUrl: String?,
    initials: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    size: Dp = 100.dp
) {
    Box(
        modifier = modifier.clickable { onClick() }
    ) {
        CircularAvatarWithInitials(
            imageUrl = imageUrl,
            initials = initials,
            size = size
        )

        // Badge de editar con icono de cámara
        Box(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .size(32.dp)
        ) {
            FloatingActionButton(
                onClick = onClick,
                modifier = Modifier.size(32.dp),
                containerColor = MaterialTheme.colorScheme.primary
            ) {
                Icon(
                    imageVector = Icons.Filled.CameraAlt,
                    contentDescription = "Cambiar foto",
                    modifier = Modifier.size(16.dp)
                )
            }
        }
    }
}