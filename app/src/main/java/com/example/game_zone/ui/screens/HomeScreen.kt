package com.example.game_zone.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
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
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navController: NavController,
    viewModel: MainViewModel = viewModel()
) {

    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    // 🎨 Paleta Sonic
    val sonicBlue = Color(0xFF0066FF)
    val sonicLightBlue = Color(0xFF00CCFF)
    val sonicYellow = Color(0xFFFFD700)
    val sonicDark = Color(0xFF021526)

    Box(modifier = Modifier.fillMaxSize()) {

        // 🔵 Fondo Sonic
        Image(
            painter = painterResource(id = R.drawable.sonic_fondo),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop,
            alpha = 0.35f
        )

        ModalNavigationDrawer(
            drawerState = drawerState,
            drawerContent = {
                ModalDrawerSheet(
                    modifier = Modifier.background(sonicDark)
                ) {
                    Text(
                        "Menu",
                        modifier = Modifier.padding(20.dp),
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = sonicLightBlue
                    )
                    HorizontalDivider(color = sonicBlue)

                    NavigationDrawerItem(
                        label = { Text("Ir al perfil", color = sonicLightBlue) },
                        selected = false,
                        onClick = {
                            scope.launch { drawerState.close() }
                            viewModel.navigateTo(Screen.Profile)
                        }
                    )

                    NavigationDrawerItem(
                        label = { Text("Ir a configuración", color = sonicLightBlue) },
                        selected = false,
                        onClick = {
                            scope.launch { drawerState.close() }
                            viewModel.navigateTo(Screen.Settings)
                        }
                    )
                }
            }
        ) {

            Scaffold(
                containerColor = Color.Transparent,
                topBar = {
                    TopAppBar(
                        title = {
                            Text(
                                "GAME ZONE",
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 2.sp,
                                color = sonicYellow
                            )
                        },
                        navigationIcon = {
                            IconButton(onClick = { scope.launch { drawerState.open() } }) {
                                Icon(Icons.Filled.Menu, contentDescription = "Menú", tint = sonicLightBlue)
                            }
                        },
                        actions = {
                            IconButton(onClick = { }) {
                                Icon(Icons.Filled.Search, contentDescription = "Buscar", tint = sonicLightBlue)
                            }
                            IconButton(onClick = { }) {
                                Icon(Icons.Filled.ShoppingCart, contentDescription = "Carrito", tint = sonicYellow)
                            }
                        },
                        colors = TopAppBarDefaults.topAppBarColors(
                            containerColor = Color(0xFF031A2C)
                        )
                    )
                }
            ) { innerPadding ->

                LazyColumn(
                    modifier = Modifier
                        .padding(innerPadding)
                        .fillMaxSize()
                ) {

                    // ⭐ BANNER PRINCIPAL
                    item {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(200.dp)
                                .padding(16.dp)
                                .shadow(12.dp, RoundedCornerShape(20.dp))
                                .background(
                                    Brush.linearGradient(listOf(sonicBlue, sonicLightBlue)),
                                    RoundedCornerShape(20.dp)
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(
                                    "¡OFERTAS ÉPICAS!",
                                    fontSize = 28.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = sonicYellow
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    "Hasta 50% de descuento",
                                    fontSize = 16.sp,
                                    color = Color.White
                                )
                            }
                        }
                    }

                    // ⭐ CATEGORÍAS
                    item {
                        Text(
                            "Categorías",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(16.dp),
                            color = sonicYellow
                        )
                    }

                    item {
                        LazyRow(
                            contentPadding = PaddingValues(horizontal = 16.dp),
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            items(
                                listOf(
                                    "🎮 Juegos",
                                    "🖥️ PC Gaming",
                                    "🎧 Periféricos",
                                    "👾 Consolas",
                                    "🕹️ Accesorios"
                                )
                            ) { categoria ->
                                Card(
                                    modifier = Modifier
                                        .width(120.dp)
                                        .height(80.dp),
                                    colors = CardDefaults.cardColors(
                                        containerColor = sonicDark.copy(alpha = 0.8f)
                                    ),
                                    shape = RoundedCornerShape(16.dp)
                                ) {
                                    Box(
                                        modifier = Modifier.fillMaxSize(),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(
                                            categoria,
                                            fontSize = 14.sp,
                                            fontWeight = FontWeight.Medium,
                                            color = sonicLightBlue
                                        )
                                    }
                                }
                            }
                        }
                    }

                    // ⭐ PRODUCTOS DESTACADOS
                    item {
                        Spacer(modifier = Modifier.height(24.dp))
                        Text(
                            "Productos Destacados",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(16.dp),
                            color = sonicYellow
                        )
                    }

                    // ⭐ LISTA DE PRODUCTOS
                    items(6) { index ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp, vertical = 8.dp),
                            shape = RoundedCornerShape(18.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = sonicDark.copy(alpha = 0.9f)
                            ),
                            elevation = CardDefaults.cardElevation(10.dp)
                        ) {
                            Row(
                                modifier = Modifier
                                    .padding(16.dp)
                                    .fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically
                            ) {

                                Box(
                                    modifier = Modifier
                                        .size(80.dp)
                                        .background(
                                            Brush.radialGradient(
                                                listOf(sonicBlue, sonicDark)
                                            ),
                                            RoundedCornerShape(12.dp)
                                        ),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text("🎮", fontSize = 32.sp)
                                }

                                Spacer(modifier = Modifier.width(16.dp))

                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        "Producto ${index + 1}",
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 16.sp,
                                        color = sonicLightBlue
                                    )
                                    Text(
                                        "Categoría Gaming",
                                        fontSize = 12.sp,
                                        color = Color.LightGray
                                    )
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(
                                        "$${(index + 1) * 10}.990",
                                        fontSize = 18.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = sonicYellow
                                    )
                                }

                                Button(
                                    onClick = {},
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = sonicBlue
                                    ),
                                    shape = RoundedCornerShape(12.dp)
                                ) {
                                    Text("Ver", color = Color.White)
                                }
                            }
                        }
                    }

                    item {
                        Spacer(modifier = Modifier.height(24.dp))
                    }
                }
            }
        }
    }
}
