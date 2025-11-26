package com.example.game_zone.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.game_zone.R
import com.example.game_zone.viewmodel.ProductoViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetalleProductoScreen(
    navController: NavController,
    productoViewModel: ProductoViewModel
) {
    val productoUiState by productoViewModel.uiState.collectAsStateWithLifecycle()
    val producto = productoUiState.productoSeleccionado

    // Paleta Sonic
    val sonicBlue = Color(0xFF0066FF)
    val sonicLightBlue = Color(0xFF00CCFF)
    val sonicYellow = Color(0xFFFFD700)
    val sonicDark = Color(0xFF021526)
    val sonicRed = Color(0xFFFF0000)

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
                            "⚡ DETALLES",
                            fontWeight = FontWeight.ExtraBold,
                            color = sonicYellow,
                            letterSpacing = 2.sp
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
                                Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Volver",
                                tint = Color.White
                            )
                        }
                    },
                    actions = {
                        IconButton(
                            onClick = { /* TODO: Favoritos */ },
                            modifier = Modifier
                                .shadow(6.dp, CircleShape)
                                .background(sonicRed, CircleShape)
                        ) {
                            Icon(
                                Icons.Filled.FavoriteBorder,
                                contentDescription = "Favorito",
                                tint = Color.White
                            )
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = Color(0xFF031A2C)
                    )
                )
            }
        ) { paddingValues ->
            if (producto != null) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                        .verticalScroll(rememberScrollState())
                ) {
                    // Imagen del producto con estilo ring
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(24.dp)
                    ) {
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(320.dp)
                                .shadow(20.dp, RoundedCornerShape(28.dp)),
                            shape = RoundedCornerShape(28.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = Color.Transparent
                            )
                        ) {
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .background(
                                        Brush.radialGradient(
                                            colors = listOf(
                                                sonicLightBlue,
                                                sonicBlue,
                                                sonicDark
                                            )
                                        )
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                Text("🎮", fontSize = 140.sp)
                            }
                        }

                        // Badge de categoría flotante
                        Card(
                            modifier = Modifier
                                .align(Alignment.TopEnd)
                                .padding(16.dp)
                                .shadow(8.dp, RoundedCornerShape(16.dp)),
                            colors = CardDefaults.cardColors(
                                containerColor = sonicYellow
                            ),
                            shape = RoundedCornerShape(16.dp)
                        ) {
                            Text(
                                text = producto.categoria,
                                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                                fontSize = 14.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = sonicBlue
                            )
                        }
                    }

                    // Contenido principal
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .shadow(20.dp, RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp)),
                        colors = CardDefaults.cardColors(
                            containerColor = sonicDark.copy(alpha = 0.95f)
                        ),
                        shape = RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(28.dp)
                        ) {
                            // Nombre del producto
                            Text(
                                text = producto.nombre,
                                fontSize = 28.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = sonicLightBlue,
                                lineHeight = 36.sp
                            )

                            Spacer(modifier = Modifier.height(12.dp))

                            // Rating con estrellas
                            Row(
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                repeat(5) {
                                    Icon(
                                        Icons.Filled.Star,
                                        contentDescription = null,
                                        tint = sonicYellow,
                                        modifier = Modifier.size(20.dp)
                                    )
                                }
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    "4.8",
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                                Text(
                                    " (150 reviews)",
                                    fontSize = 14.sp,
                                    color = Color.LightGray
                                )
                            }

                            Spacer(modifier = Modifier.height(24.dp))

                            // Precio destacado
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .shadow(12.dp, RoundedCornerShape(20.dp)),
                                colors = CardDefaults.cardColors(
                                    containerColor = Color(0xFF0A2540)
                                ),
                                shape = RoundedCornerShape(20.dp)
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(20.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Column {
                                        Text(
                                            "PRECIO ESPECIAL",
                                            fontSize = 12.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = sonicLightBlue,
                                            letterSpacing = 1.sp
                                        )
                                        Spacer(modifier = Modifier.height(4.dp))
                                        Row(
                                            verticalAlignment = Alignment.Bottom
                                        ) {
                                            Text(
                                                "$",
                                                fontSize = 24.sp,
                                                fontWeight = FontWeight.ExtraBold,
                                                color = sonicYellow
                                            )
                                            Text(
                                                String.format("%.2f", producto.precio),
                                                fontSize = 40.sp,
                                                fontWeight = FontWeight.ExtraBold,
                                                color = sonicYellow
                                            )
                                        }
                                    }

                                    Box(
                                        modifier = Modifier
                                            .size(60.dp)
                                            .background(
                                                Brush.radialGradient(
                                                    colors = listOf(sonicYellow, Color(0xFFFFAA00))
                                                ),
                                                CircleShape
                                            ),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(
                                            "💰",
                                            fontSize = 32.sp
                                        )
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(28.dp))

                            // Descripción
                            Card(
                                modifier = Modifier.fillMaxWidth(),
                                colors = CardDefaults.cardColors(
                                    containerColor = Color(0xFF0A2540)
                                ),
                                shape = RoundedCornerShape(16.dp)
                            ) {
                                Column(
                                    modifier = Modifier.padding(20.dp)
                                ) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Box(
                                            modifier = Modifier
                                                .size(8.dp)
                                                .background(sonicYellow, CircleShape)
                                        )
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Text(
                                            "DESCRIPCIÓN",
                                            fontSize = 16.sp,
                                            fontWeight = FontWeight.ExtraBold,
                                            color = sonicLightBlue,
                                            letterSpacing = 1.sp
                                        )
                                    }

                                    Spacer(modifier = Modifier.height(12.dp))

                                    Text(
                                        text = producto.descripcion ?: "Sin descripción disponible",
                                        fontSize = 15.sp,
                                        lineHeight = 24.sp,
                                        color = Color.White
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(28.dp))

                            // Características adicionales
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                SonicFeatureCard(
                                    icon = "🚚",
                                    title = "Envío Rápido",
                                    subtitle = "2-3 días",
                                    sonicLightBlue = sonicLightBlue,
                                    modifier = Modifier.weight(1f)
                                )
                                SonicFeatureCard(
                                    icon = "✓",
                                    title = "Garantía",
                                    subtitle = "1 año",
                                    sonicLightBlue = sonicLightBlue,
                                    modifier = Modifier.weight(1f)
                                )
                                SonicFeatureCard(
                                    icon = "🔄",
                                    title = "Devolución",
                                    subtitle = "30 días",
                                    sonicLightBlue = sonicLightBlue,
                                    modifier = Modifier.weight(1f)
                                )
                            }

                            Spacer(modifier = Modifier.height(32.dp))

                            // Botón de agregar al carrito - SONIC STYLE
                            Button(
                                onClick = { /* TODO: Agregar al carrito */ },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(64.dp)
                                    .shadow(20.dp, RoundedCornerShape(32.dp)),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = sonicYellow
                                ),
                                shape = RoundedCornerShape(32.dp)
                            ) {
                                Icon(
                                    Icons.Filled.ShoppingCart,
                                    contentDescription = null,
                                    modifier = Modifier.size(28.dp),
                                    tint = sonicBlue
                                )
                                Spacer(modifier = Modifier.width(12.dp))
                                Text(
                                    "💨 AGREGAR AL CARRITO",
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = sonicBlue,
                                    letterSpacing = 1.sp
                                )
                            }

                            Spacer(modifier = Modifier.height(20.dp))
                        }
                    }
                }
            } else {
                // Si no hay producto seleccionado
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    contentAlignment = Alignment.Center
                ) {
                    Card(
                        modifier = Modifier.padding(32.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = sonicDark.copy(alpha = 0.9f)
                        ),
                        shape = RoundedCornerShape(24.dp)
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.padding(40.dp)
                        ) {
                            Text("❌", fontSize = 80.sp)
                            Spacer(modifier = Modifier.height(20.dp))
                            Text(
                                "No hay producto seleccionado",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                color = sonicRed
                            )
                            Spacer(modifier = Modifier.height(24.dp))
                            Button(
                                onClick = { navController.navigateUp() },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = sonicBlue
                                ),
                                shape = RoundedCornerShape(16.dp)
                            ) {
                                Text("⚡ Volver", fontWeight = FontWeight.Bold, color = Color.White)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun SonicFeatureCard(
    icon: String,
    title: String,
    subtitle: String,
    sonicLightBlue: Color,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF0A2540)
        ),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(icon, fontSize = 28.sp)
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                title,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = sonicLightBlue
            )
            Text(
                subtitle,
                fontSize = 11.sp,
                color = Color.LightGray
            )
        }
    }
}