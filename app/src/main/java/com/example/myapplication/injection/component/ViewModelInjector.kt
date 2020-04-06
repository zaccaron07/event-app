package com.example.myapplication.injection.component

import com.example.myapplication.injection.module.NetworkModule
import com.example.myapplication.ui.group.GroupViewModel
import dagger.Component
import javax.inject.Singleton

/**
 * Component providing inject() methods for presenters.
 */
@Singleton
@Component(modules = [(NetworkModule::class)])
interface ViewModelInjector {
    /**
     * Injects required dependencies into the specified GroupViewModel.
     * @param groupViewModel GroupViewModel in which to inject the dependencies
     */
    fun inject(groupViewModel: GroupViewModel)

    @Component.Builder
    interface Builder {
        fun build(): ViewModelInjector

        fun networkModule(networkModule: NetworkModule): Builder
    }
}