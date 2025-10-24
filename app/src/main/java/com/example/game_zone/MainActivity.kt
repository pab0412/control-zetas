package com.example.game_zone

import UsuarioViewModel
import com.example.game_zone.ui.screens.HomeScreen
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.game_zone.ui.navegation.Screen
import com.example.game_zone.ui.screens.ProfileScreen
import com.example.game_zone.ui.screens.SettingsScreen
import com.example.game_zone.ui.theme.Game_zoneTheme
import com.example.game_zone.view_model.MainViewModel
import kotlinx.coroutines.flow.collectLatest
import androidx.compose.ui.Modifier
import com.example.game_zone.ui.navegation.NavigationEvent
import com.example.game_zone.ui.screens.RegistroScreen

@OptIn(ExperimentalMaterial3Api::class)
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Game_zoneTheme {

                // ViewModel y NavController
                val viewModel: MainViewModel = viewModel()
                val navController = rememberNavController()
                val usuarioViewModel: UsuarioViewModel = viewModel()

                // Escuchar eventos de navegación emitidos por el ViewModel
                LaunchedEffect(key1 = Unit) {
                    viewModel.navigationEvents.collectLatest { event ->
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

                // Layout base con NavHost
                Scaffold(
                    modifier = Modifier.fillMaxSize()
                ) { innerPadding ->

                    NavHost(
                        navController = navController,
                        startDestination = Screen.Home.route,
                        modifier = Modifier.padding(paddingValues = innerPadding)
                    ) {
                        composable(route = Screen.Home.route) {
                            HomeScreen(navController = navController, viewModel = viewModel)
                        }

                        composable(route = Screen.Profile.route) {
                            ProfileScreen(navController = navController, viewModel = viewModel)
                        }

                        composable(route = Screen.Settings.route) {
                            SettingsScreen(navController = navController, viewModel = viewModel)
                        }

                        composable(route = Screen.Registro.route){
                            RegistroScreen(navController= navController, viewModel = usuarioViewModel)
                        }
                    }
                }
            }
        }
    }
}