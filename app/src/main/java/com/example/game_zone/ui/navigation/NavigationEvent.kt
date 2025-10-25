package com.example.game_zone.ui.navigation

sealed class NavigationEvent{

    data class NavigationTo(
        val route: Screen,
        val popUpToRoute: Screen? = null,
        val inclusive: Boolean = false,
        val singleTop: Boolean = false
    ) : NavigationEvent()

    object PopBackStack : NavigationEvent()

    object NavigationUp: NavigationEvent()
}
