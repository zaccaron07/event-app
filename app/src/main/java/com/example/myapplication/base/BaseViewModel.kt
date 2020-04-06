package com.example.myapplication.base

import androidx.lifecycle.ViewModel
import com.example.myapplication.injection.component.DaggerViewModelInjector
import com.example.myapplication.injection.component.ViewModelInjector
import com.example.myapplication.injection.module.NetworkModule
import com.example.myapplication.ui.group.GroupViewModel

abstract class BaseViewModel : ViewModel() {
    private val injector: ViewModelInjector = DaggerViewModelInjector
        .builder()
        .networkModule(NetworkModule)
        .build()

    init {
        inject()
    }

    /**
     * Injects the required dependencies
     */
    private fun inject() {
        when (this) {
            is GroupViewModel -> injector.inject(this)
        }
    }
}