package com.example.myapplication.ui.group

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myapplication.R
import com.example.myapplication.base.BaseFragment
import com.example.myapplication.data.model.Group
import com.example.myapplication.databinding.FragmentGroupsBinding
import com.example.myapplication.utils.extension.kodeinViewModel
import org.kodein.di.DIAware
import org.kodein.di.android.x.di

class GroupsFragment : BaseFragment(), DIAware, RecyclerViewGroupsClickListener {
    override val di by di()

    private lateinit var binding: FragmentGroupsBinding
    override val _viewModel: GroupsViewModel by kodeinViewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_groups, container, false)
        binding.recyclerViewGroups.layoutManager = LinearLayoutManager(context)
        val groupsAdapter = GroupsListAdapter(this)
        binding.recyclerViewGroups.adapter = groupsAdapter

        _viewModel.groups.observe(viewLifecycleOwner, Observer { groups ->
            binding.swipeContainer.isRefreshing = false
            groupsAdapter.submitList(groups)
        })

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binds()
    }

    private fun binds() {
        binding.floatingActionButtonAddGroup.setOnClickListener {
            _viewModel.onAddGroupClick()
        }

        binding.swipeContainer.setOnRefreshListener {
            _viewModel.getUserGroups()
        }
    }

    override fun onRecyclerViewItemClick(group: Group) {
        _viewModel.selectedGroup = group
        _viewModel.navigate(R.id.action_groupsFragment_to_groupDetailFragment)
    }
}
