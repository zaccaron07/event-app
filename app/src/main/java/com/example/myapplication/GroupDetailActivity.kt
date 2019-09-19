package com.example.myapplication

import android.os.Bundle
import android.util.Log
import android.widget.Switch
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import kotlinx.android.synthetic.main.activity_group_detail.*
import kotlinx.android.synthetic.main.content_group_detail.*
import kotlinx.serialization.json.Json
import org.json.JSONObject

class GroupDetailActivity : AppCompatActivity() {
    private lateinit var group: Group
    private lateinit var contactId: String
    private val LOG_TAG = "GroupDetailActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_group_detail)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        fab.setOnClickListener { view ->
            this.save()
        }

        this.group =
            intent.extras!!.getString("group").let { Json.parse(Group.serializer(), it!!) }
        this.contactId = intent.extras!!.getString("contactId")!!

        this.initializeGroup()
    }

    private fun initializeGroup(
    ) {
        textViewName.text = this.group.name
        textViewDescription.text = this.group.description
        textViewStartTime.text = this.group.startTime
        textViewDate.text = this.group.date

        val rvGroupParticipants = this.rvGroupParticipants
        rvGroupParticipants.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)

        val rvAdapter =
            GroupParticipantsRvAdapter(this.group.contacts, { contact, switchParticipate ->
                partItemClicked(contact, switchParticipate)
            }, this.contactId)

        rvGroupParticipants.adapter = rvAdapter
    }

    private fun partItemClicked(contact: Contact, switchParticipate: Switch) {
        contact.participate = !contact.participate
        switchParticipate.isChecked = contact.participate
    }

    private fun save() {
        val queue = Volley.newRequestQueue(this)
        var url = "${APIConstants.BASE_URL}/group?contactId=${this.contactId}"

        val jsonData = Json.stringify(Group.serializer(), this.group)
        val jsonObject = JSONObject(jsonData)

        val request = JsonObjectRequest(
            Request.Method.PUT, url, jsonObject,
            Response.Listener {
                Toast.makeText(this, "Grupo atualizado!", Toast.LENGTH_SHORT).show()
                finish()
            },
            Response.ErrorListener { error ->
                Log.d(LOG_TAG, error.toString())
            })

        queue.add(request)
    }
}
