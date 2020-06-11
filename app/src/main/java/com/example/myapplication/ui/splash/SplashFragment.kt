package com.example.myapplication.ui.splash

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.myapplication.R
import com.example.myapplication.base.BaseFragment
import com.example.myapplication.ui.auth.AuthViewModel
import com.example.myapplication.utils.extension.kodeinViewModel
import org.kodein.di.DIAware
import org.kodein.di.android.x.di

class SplashFragment : BaseFragment(), DIAware {
    override val di by di()

    override val _viewModel: AuthViewModel by kodeinViewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_splash, container, false)
    }
}
