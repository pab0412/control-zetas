package com.example.game_zone.ui.screens

import androidx.compose.foundation.Image
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
import androidx.compose.ui.text.font.FontWeight
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

    LaunchedEffect(usuarioId) {
        usuarioId?.let { usuarioViewModel.cargarUsuario(it) }
    }

    if (showLogoutDialog) {
        AlertDialog(
            onDismissRequest = { showLogoutDialog = false },
            title = { Text("Cerrar Sesión") },
            text = { Text("¿Seguro que deseas cerrar sesión?") },
            confirmButton = {
                TextButton(
                    onClick = {
                        usuarioViewModel.cerrarSesion()
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

    Scaffold(
        containerColor = Color.Transparent,
        bottomBar = {
            NavigationBar(
                containerColor = Color(0xAA000000)
            ) {
                items.forEachIndexed { index, screen ->
                    NavigationBarItem(
                        selected = selectedItem == index,
                        onClick = {
                            selectedItem = index
                            mainViewModel.navigateTo(screen)
                        },
                        label = { Text(screen.route, color = Color.White) },
                        icon = {
                            Icon(
                                imageVector = if (screen == Screen.Home)
                                    Icons.Filled.Home else Icons.Filled.Person,
                                contentDescription = null,
                                tint = Color.White
                            )
                        }
                    )
                }
            }
        }
    ) { innerPadding ->

        Box(Modifier.fillMaxSize()) {

            // ⭐ FONDO COMPLETO
            Image(
                painter = painterResource(id = R.drawable.fondo_dragonball),
                contentDescription = null,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )

            Column(
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Spacer(modifier = Modifier.height(32.dp))

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

                Text(
                    text = uiState.nombre.ifEmpty { "Usuario Gamer" },
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                Text(
                    text = uiState.correo.ifEmpty { "usuario@email.com" },
                    fontSize = 14.sp,
                    color = Color.White.copy(alpha = 0.7f)
                )

                Spacer(modifier = Modifier.height(8.dp))

                uiState.direccion.takeIf { it.isNotEmpty() }?.let {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Filled.LocationOn,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(it, color = Color.White.copy(alpha = 0.8f))
                    }
                }

                Spacer(modifier = Modifier.height(32.dp))

                // ⭐ INTERESES EN CARD TRANSLÚCIDO
                if (uiState.gustos.isNotEmpty()) {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(
                            containerColor = Color(0xAAFFFFFF)
                        )
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Filled.Favorite,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.primary
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text("Mis Intereses", fontSize = 16.sp)
                            }
                            Spacer(modifier = Modifier.height(12.dp))

                            FlowRow(
                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                                verticalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                uiState.gustos.forEach {
                                    AssistChip(onClick = {}, label = { Text(it) })
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(24.dp))
                }

                Text(
                    "Opciones",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.White,
                    modifier = Modifier.fillMaxWidth()
                )

                ProfileOption(Icons.Filled.Edit, "Editar Perfil") {}
                ProfileOption(Icons.Filled.Settings, "Configuración") {}
                ProfileOption(Icons.Filled.Star, "Mis Pedidos") {}
                ProfileOption(Icons.Filled.Info, "Acerca de") {}

                Spacer(modifier = Modifier.height(16.dp))

                OutlinedButton(
                    onClick = { showLogoutDialog = true },
                    modifier = Modifier.fillMaxWidth().height(50.dp),
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = Color.Red
                    )
                ) {
                    Icon(Icons.AutoMirrored.Filled.ExitToApp, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Cerrar Sesión")
                }

                Spacer(modifier = Modifier.height(32.dp))
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
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xAAFFFFFF))
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(icon, contentDescription = null)
            Spacer(modifier = Modifier.width(16.dp))
            Text(title, modifier = Modifier.weight(1f))
            Icon(Icons.AutoMirrored.Filled.ArrowForward, contentDescription = null)
        }
    }
}
