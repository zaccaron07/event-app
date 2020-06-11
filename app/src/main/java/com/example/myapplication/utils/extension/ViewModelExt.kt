package com.example.myapplication.utils.extension

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import org.kodein.di.DI.Builder
import org.kodein.di.DIAware
import org.kodein.di.bind
import org.kodein.di.direct
import org.kodein.di.instance

inline fun <reified T : ViewModel> Builder.bindViewModel(overrides: Boolean? = null): Builder.TypeBinder<T> {
    return bind<T>(T::class.java.simpleName, overrides)
}

inline fun <reified VM : ViewModel, T> T.kodeinViewModel(): Lazy<VM> where T : DIAware, T : AppCompatActivity {
    return lazy { ViewModelProvider(this, direct.instance()).get(VM::class.java) }
}

inline fun <reified VM : ViewModel, T> T.kodeinViewModel(): Lazy<VM> where T : DIAware, T : Fragment {
    return lazy { ViewModelProvider(this, direct.instance()).get(VM::class.java) }
}