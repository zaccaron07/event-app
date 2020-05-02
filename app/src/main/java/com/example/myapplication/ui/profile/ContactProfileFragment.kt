package com.example.myapplication.ui.profile

import android.content.Context
import android.net.Uri
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

    private var listener: OnFragmentInteractionListener? = null
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

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnFragmentInteractionListener) {
            listener = context
        } else {
            throw RuntimeException(context.toString() + " must implement OnFragmentInteractionListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    interface OnFragmentInteractionListener {
        fun onFragmentInteraction(uri: Uri)
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