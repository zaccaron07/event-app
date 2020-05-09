package com.example.myapplication.utils

sealed class NavigationCommand {
    data class To(val action: Int) : NavigationCommand()
    object Back : NavigationCommand()
    data class BackTo(val destinationId: Int) : NavigationCommand()
    object ToRoot : NavigationCommand()
}