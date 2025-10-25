package com.example.game_zone.viewmodel

import androidx.lifecycle.ViewModel
import com.example.game_zone.ui.navigation.NavigationEvent
import com.example.game_zone.ui.navigation.Screen
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch


class MainViewModel : ViewModel(){

    private val _navigationEvents = MutableSharedFlow<NavigationEvent>()

    val navigationEvents: SharedFlow<NavigationEvent> = _navigationEvents.asSharedFlow()

    fun navigateTo(screen: Screen){
        CoroutineScope(Dispatchers.Main).launch {
            _navigationEvents.emit(NavigationEvent.NavigationTo(route = screen))
        }
    }

    fun navigateBack(){
        CoroutineScope(Dispatchers.Main).launch{
            _navigationEvents.emit(NavigationEvent.NavigationUp)
        }
    }

    fun navigateUp(){
        CoroutineScope(Dispatchers.Main).launch{
            _navigationEvents.emit(NavigationEvent.PopBackStack)
        }
    }
}