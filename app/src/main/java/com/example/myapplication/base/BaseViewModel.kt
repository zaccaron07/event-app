package com.example.myapplication.base

import androidx.lifecycle.ViewModel
import com.example.myapplication.utils.NavigationCommand
import com.hadilq.liveevent.LiveEvent

abstract class BaseViewModel : ViewModel() {
    val navigationCommands: LiveEvent<NavigationCommand> = LiveEvent()

    fun navigate(action: Int) {
        navigationCommands.postValue(NavigationCommand.To(action))
    }

    fun popBackStack() {
        navigationCommands.postValue(NavigationCommand.Back)
    }
}