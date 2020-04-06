package com.example.myapplication.ui.group

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myapplication.GroupDetailActivity
import com.example.myapplication.NewGroupActivity
import com.example.myapplication.R
import com.example.myapplication.databinding.FragmentGroupsBinding
import com.example.myapplication.injection.ViewModelFactory
import com.example.myapplication.model.Group
import com.example.myapplication.ui.HomeActivity
import kotlinx.serialization.json.Json

class GroupsFragment : Fragment() {
    private val clickListener: ClickListener = this::partItemClicked

    private val recyclerViewAdapter = RvAdapter(clickListener)
    private var listener: OnFragmentInteractionListener? = null
    private lateinit var binding: FragmentGroupsBinding
    private lateinit var viewModel:GroupViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_groups, container, false)
        binding.recyclerViewGroups.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        binding.recyclerViewGroups.adapter = recyclerViewAdapter

        viewModel = ViewModelProviders.of(this, ViewModelFactory(context!!)).get(GroupViewModel::class.java)
        viewModel.getGroupList().observe(viewLifecycleOwner, Observer { groups ->
            groups?.let { render(groups) }
        })

        binding.viewModel = viewModel

        binding.floatingActionButtonAddGroup.setOnClickListener {
            (context as HomeActivity).startActivity(NewGroupActivity::class.java)
        }

        return binding.root
    }

    private fun render(groupList: List<Group>) {
        recyclerViewAdapter.updateGroupList(groupList)
    }

    private fun partItemClicked(group: Group) {
        var groupData: String = Json.stringify(Group.serializer(), group)

        (context as HomeActivity).startActivity(
            GroupDetailActivity::class.java,
            groupData,
            viewModel.contact.id
        )
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
            GroupsFragment().apply {
            }
    }
}
