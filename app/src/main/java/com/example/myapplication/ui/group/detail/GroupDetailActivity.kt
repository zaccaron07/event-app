package com.example.myapplication.ui.group.detail

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.data.model.Contact
import com.example.myapplication.data.model.Group
import com.example.myapplication.databinding.ActivityGroupDetailBinding
import com.example.myapplication.ui.group.GroupsViewModel
import com.example.myapplication.utils.extension.kodeinViewModel
import kotlinx.android.synthetic.main.activity_group_detail.*
import kotlinx.serialization.json.Json
import org.kodein.di.KodeinAware
import org.kodein.di.android.kodein

class GroupDetailActivity : AppCompatActivity(), KodeinAware, RecyclerViewGroupDetailClickListener {
    override val kodein by kodein()

    private val viewModel: GroupsViewModel by kodeinViewModel()
    private lateinit var binding: ActivityGroupDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_group_detail)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        viewModel.selectedGroup =
            intent.extras!!.getString("group").let { Json.parse(Group.serializer(), it!!) }

        viewModel.currentContactId = intent.extras!!.getString("contactId")!!
        viewModel.groupDetailSaved.observe(this, Observer { groupDetailSaved ->
            if (groupDetailSaved) {
                Toast.makeText(this, "Grupo atualizado!", Toast.LENGTH_SHORT).show()
                finish()
            }
        })

        binding.viewmodel = viewModel

        this.initializeGroup()
    }

    private fun initializeGroup() {
        val rvGroupParticipants = this.rvGroupParticipants
        rvGroupParticipants.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)

        val rvAdapter =
            GroupDetailAdapter(viewModel.selectedGroup.contacts, this, viewModel.currentContactId)

        rvGroupParticipants.adapter = rvAdapter
    }

    override fun onRecyclerViewItemClick(contact: Contact) {
        contact.participate = !contact.participate
    }
}
