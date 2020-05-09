package com.example.myapplication.base

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.myapplication.utils.NavigationCommand

abstract class BaseFragment : Fragment() {

    abstract val _viewModel: BaseViewModel?

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        _viewModel?.navigationCommands?.observe(viewLifecycleOwner, Observer {
            when (it) {
                is NavigationCommand.To ->
                    findNavController().navigate(it.action)

                is NavigationCommand.Back ->
                    findNavController().popBackStack()

                is NavigationCommand.BackTo ->
                    findNavController().popBackStack(it.destinationId, false)
            }
        })
    }
}