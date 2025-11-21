package com.example.game_zone

import com.example.game_zone.viewmodel.EstadoViewModel
import com.example.game_zone.ui.screens.HomeScreen
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.game_zone.data.database.GameZoneDatabase
import com.example.game_zone.data.repository.UsuarioRepository
import com.example.game_zone.ui.navigation.Screen
import com.example.game_zone.ui.screens.ProfileScreen
import com.example.game_zone.ui.screens.SettingsScreen
import com.example.game_zone.ui.theme.Game_zoneTheme
import com.example.game_zone.viewmodel.MainViewModel
import com.example.game_zone.viewmodel.UsuarioViewModel
import kotlinx.coroutines.flow.collectLatest
import androidx.compose.ui.Modifier
import androidx.core.view.WindowCompat
import com.example.game_zone.ui.navigation.NavigationEvent
import com.example.game_zone.ui.screens.EditarPerfilScreen
import com.example.game_zone.ui.screens.LoginScreen
import com.example.game_zone.ui.screens.PantallaCarga
import com.example.game_zone.ui.screens.RegistroScreen

@OptIn(ExperimentalMaterial3Api::class)
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)

        // Inicio de la base de datos junto al repositorio.
        val database = GameZoneDatabase.getDatabase(applicationContext)
        val repository = UsuarioRepository(database.usuarioDao())
        val viewModelFactory = UsuarioViewModel.UsuarioViewModelFactory(repository)
        val usuarioViewModel = ViewModelProvider(this, viewModelFactory)[UsuarioViewModel::class.java]

        setContent {
            Game_zoneTheme {
                // ViewModels
                val mainViewModel: MainViewModel = viewModel()
                val estadoViewModel: EstadoViewModel = viewModel()
                val navController = rememberNavController()

                // Obtener estado de carga
                val activo by estadoViewModel.activo.collectAsState()

                // Escuchar eventos de navegación emitidos por el ViewModel
                LaunchedEffect(key1 = Unit) {
                    mainViewModel.navigationEvents.collectLatest { event ->
                        when (event) {
                            is NavigationEvent.NavigationTo -> {
                                navController.navigate(route = event.route.route) {
                                    event.popUpToRoute?.let {
                                        popUpTo(route = it.route) {
                                            inclusive = event.inclusive
                                        }
                                    }
                                    launchSingleTop = event.singleTop
                                    restoreState = true
                                }
                            }
                            is NavigationEvent.PopBackStack -> navController.popBackStack()
                            is NavigationEvent.NavigationUp -> navController.navigateUp()
                        }
                    }
                }

                if (activo == null) {
                    PantallaCarga(
                        modifier = Modifier.fillMaxSize(),
                        viewModel = estadoViewModel
                    )
                } else {
                    Scaffold(
                        modifier = Modifier.fillMaxSize()
                    ) { innerPadding ->
                        NavHost(
                            navController = navController,
                            startDestination = Screen.Login.route,
                            modifier = Modifier.padding(paddingValues = innerPadding)
                        ) {
                            composable(route = Screen.Login.route) {
                                LoginScreen(
                                    navController = navController,
                                    viewModel = usuarioViewModel
                                )
                            }

                            composable(route = Screen.Registro.route) {
                                RegistroScreen(
                                    navController = navController,
                                    viewModel = usuarioViewModel
                                )
                            }

                            composable(route = Screen.Home.route) {
                                HomeScreen(
                                    navController = navController,
                                    viewModel = mainViewModel
                                )
                            }

                            composable(route = Screen.Profile.route) {
                                ProfileScreen(
                                    navController = navController,
                                    mainViewModel = mainViewModel,
                                    usuarioViewModel = usuarioViewModel
                                )
                            }

                            composable(route = Screen.Editar.route) {
                                EditarPerfilScreen(
                                    navController = navController,
                                    viewModel = usuarioViewModel
                                )
                            }

                            composable(route = Screen.Settings.route) {
                                SettingsScreen(
                                    navController = navController,
                                    viewModel = mainViewModel
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}