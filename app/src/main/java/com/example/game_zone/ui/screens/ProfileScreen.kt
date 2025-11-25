package com.example.game_zone.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.game_zone.R
import com.example.game_zone.ui.components.CircularAvatarClickable
import com.example.game_zone.ui.components.CameraImagePicker
import com.example.game_zone.ui.navigation.Screen
import com.example.game_zone.viewmodel.MainViewModel
import com.example.game_zone.viewmodel.UsuarioViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    navController: NavController,
    mainViewModel: MainViewModel = viewModel(),
    usuarioViewModel: UsuarioViewModel
) {
    val items = listOf(Screen.Home, Screen.Profile)
    var selectedItem by remember { mutableStateOf(1) }
    val uiState by usuarioViewModel.estado.collectAsState()
    val usuarioId by usuarioViewModel.usuarioActualId.collectAsState()
    var showImageOptions by remember { mutableStateOf(false) }
    var showLogoutDialog by remember { mutableStateOf(false) }

    // Cargar datos del usuario
    LaunchedEffect(usuarioId) {
        usuarioId?.let { usuarioViewModel.cargarUsuario(it) }
    }

    // Diálogo cerrar sesión
    if (showLogoutDialog) {
        AlertDialog(
            onDismissRequest = { showLogoutDialog = false },
            title = { Text("Cerrar Sesión") },
            text = { Text("¿Estás seguro que deseas cerrar sesión?") },
            confirmButton = {
                TextButton(
                    onClick = {
                        usuarioViewModel.cerrarSesion()
                        showLogoutDialog = false
                        navController.navigate(Screen.Login.route) {
                            popUpTo(0) { inclusive = true }
                        }
                    }
                ) { Text("Sí, cerrar sesión") }
            },
            dismissButton = {
                TextButton(onClick = { showLogoutDialog = false }) {
                    Text("Cancelar")
                }
            }
        )
    }

    Box(modifier = Modifier.fillMaxSize()) {

        // 🔥🔥  DRAGON BALL Z 🔥🔥
        Image(
            painter = painterResource(id = R.drawable.fondo_dragonball),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        // Sombra oscura para que se lea el contenido
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0x99000000))
        )

        Scaffold(
            containerColor = Color.Transparent,
            bottomBar = {
                NavigationBar(
                    containerColor = Color(0xCC1A1A1A)
                ) {
                    items.forEachIndexed { index, screen ->
                        NavigationBarItem(
                            selected = selectedItem == index,
                            onClick = {
                                selectedItem = index
                                mainViewModel.navigateTo(screen)
                            },
                            label = {
                                Text(
                                    text = screen.route,
                                    color = if (selectedItem == index)
                                        Color(0xFFFFA500) else Color.White
                                )
                            },
                            icon = {
                                Icon(
                                    imageVector = if (screen == Screen.Home)
                                        Icons.Filled.Home else Icons.Filled.Person,
                                    contentDescription = screen.route,
                                    tint = if (selectedItem == index)
                                        Color(0xFFFFA500) else Color.White
                                )
                            }
                        )
                    }
                }
            }
        ) { innerPadding ->
            Column(
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(32.dp))

                // Foto de perfil
                CircularAvatarClickable(
                    imageUrl = uiState.imagen,
                    initials = uiState.nombre.take(2).uppercase().ifEmpty { "UG" },
                    onClick = { showImageOptions = true },
                    size = 100.dp
                )

                CameraImagePicker(
                    currentImageUrl = uiState.imagen,
                    onImageSelected = {
                        usuarioViewModel.onImagenChange(it)
                        usuarioViewModel.actualizarUsuario()
                    },
                    showOptions = showImageOptions,
                    onDismiss = { showImageOptions = false }
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Nombre estilo DBZ
                Text(
                    text = uiState.nombre.ifEmpty { "Guerrero Z" },
                    fontSize = 26.sp,
                    color = Color(0xFFFFA500),
                    fontWeight = androidx.compose.ui.text.font.FontWeight.ExtraBold
                )

                Text(
                    text = uiState.correo,
                    fontSize = 14.sp,
                    color = Color(0xFFFFD27F)
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Dirección
                if (uiState.direccion.isNotEmpty()) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Filled.LocationOn,
                            contentDescription = "Dirección",
                            tint = Color(0xFFFFA500),
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = uiState.direccion,
                            fontSize = 12.sp,
                            color = Color.White
                        )
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Intereses estilo cápsulas DBZ
                if (uiState.gustos.isNotEmpty()) {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(
                            containerColor = Color(0xAA1A1A1A)
                        )
                    ) {
                        Column(Modifier.padding(16.dp)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Filled.Star,
                                    contentDescription = "Intereses",
                                    tint = Color(0xFFFFA500)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "Intereses",
                                    color = Color.White,
                                    fontSize = 16.sp
                                )
                            }
                            Spacer(modifier = Modifier.height(12.dp))

                            FlowRow(
                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                                verticalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                uiState.gustos.forEach {
                                    AssistChip(
                                        onClick = {},
                                        label = { Text(it, color = Color.Black) },
                                        colors = AssistChipDefaults.assistChipColors(
                                            containerColor = Color(0xFFFFA500)
                                        )
                                    )
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Opciones
                Text(
                    text = "Opciones",
                    color = Color.White,
                    fontSize = 18.sp,
                    fontWeight = androidx.compose.ui.text.font.FontWeight.Bold,
                    modifier = Modifier.fillMaxWidth()
                )

                ProfileOption(
                    icon = Icons.Filled.Edit,
                    title = "Editar Perfil",
                    onClick = { navController.navigate(Screen.Editar.route) }
                )

                ProfileOption(
                    icon = Icons.Filled.Settings,
                    title = "Configuración",
                    onClick = { }
                )

                ProfileOption(
                    icon = Icons.Filled.Info,
                    title = "Acerca de",
                    onClick = { }
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Cerrar sesión
                OutlinedButton(
                    onClick = { showLogoutDialog = true },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = Color(0xFFFF5E5E)
                    )
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ExitToApp,
                        contentDescription = "Cerrar sesión"
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Cerrar Sesión")
                }

                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }
}

@Composable
fun ProfileOption(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    onClick: () -> Unit
) {
    Card(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xBB1A1A1A))
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = title,
                tint = Color(0xFFFFA500)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Text(text = title, color = Color.White, fontSize = 16.sp)
            Spacer(modifier = Modifier.weight(1f))
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                contentDescription = "Ir",
                tint = Color(0x66FFFFFF)
            )
        }
    }
}
