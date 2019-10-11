package com.example.myapplication

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.Volley
import kotlinx.android.synthetic.main.fragment_groups.view.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json

class GroupsFragment : Fragment() {
    private var db: AppDatabase? = null
    private var listener: OnFragmentInteractionListener? = null
    private lateinit var contact: Contact
    private val LOG_TAG = "GroupsFragment"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        db = AppDatabase.invoke(context!!)

        getUserGroups()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_groups, container, false)

        view.floatingActionButtonAddGroup.setOnClickListener {
            (context as HomeActivity).startActivity(NewGroupActivity::class.java)
        }

        return view
    }

    private fun getUserGroups() {
        val queue = Volley.newRequestQueue(context)

        GlobalScope.launch {
            contact = db?.contactDao()?.getContact()!!

            var url = "${APIConstants.BASE_URL}/contact/${contact?.id}/group"

            val request = JsonArrayRequest(
                Request.Method.GET, url, null,
                Response.Listener { response ->
                    val recyclerView = view?.recyclerViewGroups

                    recyclerView?.layoutManager =
                        LinearLayoutManager(context, RecyclerView.VERTICAL, false)
                    val groupUtils = GroupUtils()
                    val dataList = groupUtils.formatJsonGroup(response.toString())

                    val rvAdapter =
                        RvAdapter(dataList) { group: Group -> partItemClicked(group) }
                    recyclerView?.adapter = rvAdapter
                },
                Response.ErrorListener { error ->
                    Log.d(LOG_TAG, error.toString())
                })

            queue.add(request)
        }
    }

    private fun partItemClicked(group: Group) {
        var groupData: String = Json.stringify(Group.serializer(), group)

        (context as HomeActivity).startActivity(
            GroupDetailActivity::class.java,
            groupData,
            this.contact.id
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
