package com.example.game_zone.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.ShoppingCart
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
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.game_zone.R
import com.example.game_zone.data.entity.ProductoEntity
import com.example.game_zone.ui.navigation.Screen
import com.example.game_zone.viewmodel.ProductoViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductoScreen(
    navController: NavController,
    viewModel: ProductoViewModel
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    var searchText by remember { mutableStateOf(TextFieldValue("")) }

    // Paleta Sonic
    val sonicBlue = Color(0xFF0066FF)
    val sonicLightBlue = Color(0xFF00CCFF)
    val sonicYellow = Color(0xFFFFD700)
    val sonicDark = Color(0xFF021526)
    val sonicRed = Color(0xFFFF0000)

    LaunchedEffect(searchText.text) {
        if (searchText.text.isNotEmpty()) {
            viewModel.buscarProductos(searchText.text)
        } else {
            viewModel.obtenerTodosLosProductos()
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        // Fondo Sonic
        Image(
            painter = painterResource(id = R.drawable.sonic_fondo),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop,
            alpha = 0.35f
        )

        Scaffold(
            containerColor = Color.Transparent,
            topBar = {
                TopAppBar(
                    title = {
                        Text(
                            "ZONA DE PRODUCTOS",
                            fontWeight = FontWeight.ExtraBold,
                            letterSpacing = 2.sp,
                            color = sonicYellow
                        )
                    },
                    navigationIcon = {
                        IconButton(
                            onClick = { navController.navigateUp() },
                            modifier = Modifier
                                .padding(8.dp)
                                .shadow(8.dp, CircleShape)
                                .background(sonicBlue, CircleShape)
                        ) {
                            Icon(
                                Icons.Filled.ArrowBack,
                                contentDescription = "Volver",
                                tint = Color.White
                            )
                        }
                    },
                    actions = {
                        IconButton(
                            onClick = { /* TODO: Carrito */ },
                            modifier = Modifier
                                .shadow(6.dp, CircleShape)
                                .background(sonicYellow, CircleShape)
                        ) {
                            Icon(
                                Icons.Filled.ShoppingCart,
                                contentDescription = "Carrito",
                                tint = sonicBlue
                            )
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = Color(0xFF031A2C)
                    )
                )
            }
        ) { innerPadding ->
            Column(
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize()
            ) {
                // Barra de búsqueda estilo Sonic
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                        .shadow(12.dp, RoundedCornerShape(24.dp)),
                    colors = CardDefaults.cardColors(
                        containerColor = Color.White
                    ),
                    shape = RoundedCornerShape(24.dp)
                ) {
                    TextField(
                        value = searchText,
                        onValueChange = { searchText = it },
                        modifier = Modifier.fillMaxWidth(),
                        label = {
                            Text(
                                "💨 Busca a toda velocidad...",
                                color = sonicBlue
                            )
                        },
                        leadingIcon = {
                            Icon(
                                Icons.Default.Search,
                                contentDescription = null,
                                tint = sonicLightBlue
                            )
                        },
                        singleLine = true,
                        colors = TextFieldDefaults.colors(
                            unfocusedContainerColor = Color.Transparent,
                            focusedContainerColor = Color.Transparent,
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent,
                            focusedTextColor = sonicDark,
                            unfocusedTextColor = sonicDark
                        )
                    )
                }

                // Categorías estilo anillos dorados
                LazyRow(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    val categorias = listOf("Electrónica", "Accesorios", "Almacenamiento", "Periféricos")
                    items(categorias.size) { index ->
                        val isSelected = uiState.categoriaPorFiltrar == categorias[index]
                        Card(
                            onClick = { viewModel.filtrarPorCategoria(categorias[index]) },
                            modifier = Modifier
                                .height(48.dp)
                                .shadow(
                                    elevation = if (isSelected) 12.dp else 6.dp,
                                    shape = RoundedCornerShape(24.dp)
                                ),
                            colors = CardDefaults.cardColors(
                                containerColor = if (isSelected) sonicYellow else sonicDark.copy(alpha = 0.9f)
                            ),
                            shape = RoundedCornerShape(24.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(horizontal = 20.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    categorias[index],
                                    fontSize = 14.sp,
                                    fontWeight = if (isSelected) FontWeight.ExtraBold else FontWeight.Bold,
                                    color = if (isSelected) sonicBlue else sonicLightBlue
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Contenido principal
                when {
                    uiState.cargando -> {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                CircularProgressIndicator(
                                    color = sonicYellow,
                                    strokeWidth = 4.dp,
                                    modifier = Modifier.size(60.dp)
                                )
                                Spacer(modifier = Modifier.height(16.dp))
                                Text(
                                    "Cargando a máxima velocidad...",
                                    color = sonicLightBlue,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 16.sp
                                )
                            }
                        }
                    }
                    uiState.error != null -> {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Card(
                                modifier = Modifier.padding(24.dp),
                                colors = CardDefaults.cardColors(
                                    containerColor = sonicDark.copy(alpha = 0.9f)
                                ),
                                shape = RoundedCornerShape(20.dp)
                            ) {
                                Column(
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    modifier = Modifier.padding(32.dp)
                                ) {
                                    Text("X", fontSize = 60.sp)
                                    Spacer(modifier = Modifier.height(16.dp))
                                    Text(
                                        text = uiState.error ?: "Error desconocido",
                                        color = sonicRed,
                                        fontSize = 16.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Spacer(modifier = Modifier.height(24.dp))
                                    Button(
                                        onClick = { viewModel.obtenerTodosLosProductos() },
                                        colors = ButtonDefaults.buttonColors(
                                            containerColor = sonicBlue
                                        ),
                                        shape = RoundedCornerShape(16.dp)
                                    ) {
                                        Text("Reintentar", color = Color.White, fontWeight = FontWeight.Bold)
                                    }
                                }
                            }
                        }
                    }
                    uiState.productos.isEmpty() -> {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Card(
                                modifier = Modifier.padding(24.dp),
                                colors = CardDefaults.cardColors(
                                    containerColor = sonicDark.copy(alpha = 0.9f)
                                ),
                                shape = RoundedCornerShape(20.dp)
                            ) {
                                Column(
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    modifier = Modifier.padding(32.dp)
                                ) {
                                    Text("🔍", fontSize = 60.sp)
                                    Spacer(modifier = Modifier.height(16.dp))
                                    Text(
                                        text = "No hay productos disponibles",
                                        fontSize = 16.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = sonicLightBlue
                                    )
                                }
                            }
                        }
                    }
                    else -> {
                        LazyColumn(
                            modifier = Modifier.fillMaxSize(),
                            verticalArrangement = Arrangement.spacedBy(12.dp),
                            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
                        ) {
                            items(uiState.productos) { producto ->
                                SonicProductoCard(
                                    producto = producto,
                                    sonicBlue = sonicBlue,
                                    sonicLightBlue = sonicLightBlue,
                                    sonicYellow = sonicYellow,
                                    sonicDark = sonicDark,
                                    onClick = {
                                        viewModel.seleccionarProducto(producto)
                                        navController.navigate(Screen.DetalleProducto.route)
                                    }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun SonicProductoCard(
    producto: ProductoEntity,
    sonicBlue: Color,
    sonicLightBlue: Color,
    sonicYellow: Color,
    sonicDark: Color,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(12.dp, RoundedCornerShape(20.dp)),
        elevation = CardDefaults.cardElevation(10.dp),
        onClick = onClick,
        colors = CardDefaults.cardColors(
            containerColor = sonicDark.copy(alpha = 0.95f)
        ),
        shape = RoundedCornerShape(20.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Imagen placeholder con gradiente
            Box(
                modifier = Modifier
                    .size(100.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(
                        Brush.radialGradient(
                            colors = listOf(sonicLightBlue, sonicBlue, sonicDark)
                        )
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text("🎮", fontSize = 48.sp)
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = producto.nombre,
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 17.sp,
                    color = sonicLightBlue,
                    maxLines = 2
                )

                Spacer(modifier = Modifier.height(4.dp))

                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = sonicBlue.copy(alpha = 0.3f)
                    ),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        text = producto.categoria,
                        fontSize = 12.sp,
                        color = sonicYellow,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = producto.descripcion ?: "",
                    fontSize = 12.sp,
                    color = Color.LightGray,
                    maxLines = 1
                )

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "$",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = sonicYellow
                    )
                    Text(
                        text = String.format("%.2f", producto.precio),
                        fontSize = 22.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = sonicYellow
                    )
                }
            }

            Spacer(modifier = Modifier.width(12.dp))

            // Botón acción
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Button(
                    onClick = onClick,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = sonicBlue
                    ),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.shadow(8.dp, RoundedCornerShape(12.dp))
                ) {
                    Text("Ver", color = Color.White, fontWeight = FontWeight.Bold)
                }

                IconButton(
                    onClick = { /* TODO: Agregar al carrito */ },
                    modifier = Modifier
                        .size(48.dp)
                        .shadow(8.dp, CircleShape)
                        .background(sonicYellow, CircleShape)
                ) {
                    Icon(
                        Icons.Filled.ShoppingCart,
                        contentDescription = "Agregar",
                        tint = sonicBlue
                    )
                }
            }
        }
    }
}