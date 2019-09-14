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
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_group_detail.*
import kotlinx.android.synthetic.main.content_group_detail.*
import kotlinx.serialization.json.Json
import org.json.JSONArray
import org.json.JSONObject

class GroupDetailActivity : AppCompatActivity() {
    private var group: GroupDetail? = null
    private var userGroupDetail = UserGroupDetail()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_group_detail)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        fab.setOnClickListener { view ->
            Log.d("GroupDetailSave", this.userGroupDetail.toString())
            this.save()
        }

        group = intent.extras?.getString("group")?.let { Json.parse(GroupDetail.serializer(), it) }
        this.textViewName?.text = group?.name
        this.textViewDescription?.text = group?.description
        this.textViewStartTime?.text = group?.startTime
        this.textViewDate?.text = group?.date

        this.getGroupUsers(group!!.id)
    }

    private fun getGroupUsers(groupId: String) {
        val queue = Volley.newRequestQueue(this)
        var url = "http://192.168.15.11:3000/contact/${groupId}"


        var jsonArray = JSONArray()

        val request = JsonArrayRequest(
            Request.Method.GET, url, null,
            Response.Listener { response ->
                Log.d("GroupDetailGetGroupUser", response.toString())

                val dataList = ArrayList<UserGroupDetail>()

                for (index in 0 until response.length()) {
                    val userGroupDetail = UserGroupDetail()
                    val jsonObject = response.getJSONObject(index)
                    val jsonObjectGroup = (jsonObject.getJSONArray("groups").get(0) as JSONObject)
                    userGroupDetail.id = jsonObject["id"].toString()
                    userGroupDetail.name = jsonObject["name"].toString()
                    userGroupDetail.groupId = groupId
                    userGroupDetail.participate =
                        (jsonObjectGroup["participe"] as Boolean)
                    userGroupDetail.permission = (jsonObjectGroup["permisson"] as Int)

                    dataList.add(userGroupDetail)
                }

                initializeList(dataList)
            },
            Response.ErrorListener { error ->
                Log.d("GroupDetailGetGroupUser", error.toString())
            })

        queue.add(request)
    }

    private fun initializeList(
        userGroupDetailList: ArrayList<UserGroupDetail>
    ) {
        val rvGroupParticipants = this.rvGroupParticipants
        rvGroupParticipants.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)

        val rvAdapter =
            GroupParticipantsRvAdapter(userGroupDetailList) { userGroupDetail, switchParticipate ->
                partItemClicked(userGroupDetail, switchParticipate)
            }

        rvGroupParticipants.adapter = rvAdapter
    }

    private fun partItemClicked(userGroupDetail: UserGroupDetail, switchParticipate: Switch) {
        userGroupDetail.participate = !userGroupDetail.participate
        switchParticipate.isChecked = userGroupDetail.participate
        this.userGroupDetail = userGroupDetail
        Toast.makeText(this, userGroupDetail.name, Toast.LENGTH_SHORT).show()
    }

    private fun save(){
        val queue = Volley.newRequestQueue(this)
        var url = "http://192.168.15.11:3000/contact/${this.userGroupDetail.groupId}"

        val jsonData = Json.stringify(UserGroupDetail.serializer(), this.userGroupDetail)
        val jsonObject = JSONObject(jsonData)
        Log.d("GroupDetailSave", jsonObject.toString())

        val request = JsonObjectRequest(
            Request.Method.PUT, url, jsonObject,
            Response.Listener { response ->
                Log.d("GroupDetailSave", response.toString())
            },
            Response.ErrorListener { error ->
                Log.d("GroupDetailSave", error.toString())
            })

        queue.add(request)
    }
}
