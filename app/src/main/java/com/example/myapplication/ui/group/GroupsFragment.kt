package com.example.myapplication.ui.group

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myapplication.R
import com.example.myapplication.base.BaseFragment
import com.example.myapplication.data.model.Group
import com.example.myapplication.databinding.FragmentGroupsBinding
import com.example.myapplication.utils.Result
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
        binding.recyclerViewGroups.adapter = GroupsListAdapter(this)

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

        _viewModel.groups.observe(viewLifecycleOwner, Observer { result ->
            when (result) {
                is Result.Success -> {
                    binding.swipeContainer.isRefreshing = false
                    (binding.recyclerViewGroups.adapter as GroupsListAdapter).submitList(result.extractData)
                }
                is Result.InProgress -> {
                    binding.swipeContainer.isRefreshing = true
                }
                is Result.Error -> {
                    binding.swipeContainer.isRefreshing = false
                    Toast.makeText(context, result.exception.message, Toast.LENGTH_LONG).show()
                }
            }
        })
    }

    override fun onRecyclerViewItemClick(group: Group) {
        _viewModel.selectedGroup = group
        _viewModel.navigate(R.id.action_groupsFragment_to_groupDetailFragment)
    }
}
