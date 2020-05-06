package com.example.myapplication.ui.group

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myapplication.R
import com.example.myapplication.data.model.Group
import com.example.myapplication.databinding.FragmentGroupsBinding
import com.example.myapplication.utils.extension.kodeinViewModel
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.kodein

class GroupsFragment : Fragment(), KodeinAware, RecyclerViewGroupsClickListener {
    override val kodein by kodein()

    private var recyclerViewAdapter = GroupsAdapter(this)
    private lateinit var binding: FragmentGroupsBinding
    private val viewModel: GroupsViewModel by kodeinViewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_groups, container, false)
        binding.recyclerViewGroups.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        binding.recyclerViewGroups.adapter = recyclerViewAdapter

        viewModel.groups.observe(viewLifecycleOwner, Observer { groups ->
            groups?.let { render(groups) }
        })

        binding.viewModel = viewModel

        binding.floatingActionButtonAddGroup.setOnClickListener {
            findNavController().navigate(R.id.action_groupsFragment_to_groupFragment)
        }

        return binding.root
    }

    private fun render(groupList: List<Group>) {
        recyclerViewAdapter.updateGroupList(groupList)
    }

    override fun onRecyclerViewItemClick(group: Group) {
        viewModel.selectedGroup = group

        findNavController().navigate(R.id.action_groupsFragment_to_groupDetailFragment)
    }
}
