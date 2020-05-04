package com.example.myapplication.ui.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.example.myapplication.R
import com.example.myapplication.databinding.FragmentUserProfileBinding
import com.example.myapplication.ui.HomeActivity
import com.example.myapplication.ui.group.GroupsFragment
import com.example.myapplication.utils.extension.kodeinViewModel
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.kodein

class ContactProfileFragment : Fragment(), KodeinAware {

    override val kodein by kodein()

    private val viewModel: ContactProfileViewModel by kodeinViewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding: FragmentUserProfileBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_user_profile, container, false)

        viewModel.success.observe(viewLifecycleOwner, Observer { success ->
            if (success) {
                (activity as HomeActivity).replaceFragment(GroupsFragment.newInstance())
            }
        })

        binding.viewmodel = viewModel

        return binding.root
    }

    companion object {

        @JvmStatic
        fun newInstance() =
            ContactProfileFragment().apply {
                arguments = Bundle().apply {
                }
            }
    }
}