package com.example.myapplication.ui.group.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myapplication.R
import com.example.myapplication.data.model.Contact
import com.example.myapplication.databinding.FragmentGroupDetailBinding
import com.example.myapplication.ui.group.GroupsViewModel
import com.example.myapplication.utils.extension.kodeinViewModel
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.kodein

class GroupDetailFragment : Fragment(), KodeinAware, RecyclerViewGroupDetailClickListener {

    override val kodein by kodein()

    private val viewModel: GroupsViewModel by kodeinViewModel()
    private lateinit var binding: FragmentGroupDetailBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_group_detail, container, false)

        binding.recyclerViewGroupParticipants.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)

        binding.recyclerViewGroupParticipants.adapter =
            GroupDetailAdapter(viewModel.selectedGroup.contacts, this, viewModel.contact.id)

        viewModel.groupDetailSaved.observe(viewLifecycleOwner, Observer { it ->
            if (it) {
                viewModel.groupDetailSaved.value = false
                Toast.makeText(context, "Grupo atualizado!", Toast.LENGTH_SHORT).show()
                findNavController().popBackStack()
            }
        })

        binding.viewmodel = viewModel

        return binding.root
    }

    override fun onRecyclerViewItemClick(contact: Contact) {
        contact.participate = !contact.participate
    }
}
