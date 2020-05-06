package com.example.myapplication.ui.splash

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.myapplication.R
import com.example.myapplication.ui.auth.AuthViewModel
import com.example.myapplication.utils.extension.kodeinViewModel
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.kodein

class SplashFragment : Fragment(), KodeinAware {
    override val kodein by kodein()

    private val viewModel: AuthViewModel by kodeinViewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_splash, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (viewModel.authenticationState == AuthViewModel.AuthenticationState.AUTHENTICATED) {
            findNavController().navigate(R.id.action_splashFragment_to_groupsFragment)
        } else {
            findNavController().navigate(R.id.action_splashFragment_to_loginFragment)
        }
    }
}
