package com.example.myapplication

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import kotlinx.android.synthetic.main.fragment_groups.view.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import org.json.JSONArray

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [GroupsFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [GroupsFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class GroupsFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private var listener: OnFragmentInteractionListener? = null
    private var db: AppDatabase? = null
    private lateinit var listView: ListView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }

        db = AppDatabase.invoke(context!!)

        getUserGroups()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
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
        var url = "http://192.168.15.11:3000/contactUser/"

        GlobalScope.launch {
            var user = db?.userDao()?.getUser()

            url += user?.phoneNumber

            val request = JsonObjectRequest(
                Request.Method.GET, url, null,
                Response.Listener { response ->
                    Log.d("groupsFragment", response.toString())
                    var userData: User =
                        Json.nonstrict.parse(User.serializer(), response.toString())
                    getGroups(userData)
                },
                Response.ErrorListener { error ->
                    Log.d("groupsFragment", error.toString())
                })

            queue.add(request)
        }

    }

    private fun partItemClicked(group: GroupDetail) {
        Toast.makeText(context, "Clicked: ${group.name}", Toast.LENGTH_LONG).show()
        var groupData: String = Json.stringify(GroupDetail.serializer(), group)

        (context as HomeActivity).startActivity(GroupDetailActivity::class.java, groupData)
    }

    private fun getGroups(user: User) {
        val queue = Volley.newRequestQueue(context)
        var url = "http://192.168.15.11:3000/groupUsuario"

        GlobalScope.launch {
            var jsonArray = JSONArray()
            var q = "?groupId="
            user.groups.forEach {
                jsonArray.put(it.group)
                q += it.group + ","
            }

            q = q.substring(0, q.length - 1)
            url += q

            val request = JsonArrayRequest(
                Request.Method.GET, url, null,
                Response.Listener { response ->
                    Log.d("groupsFragment", response.toString())

                    val recyclerView = view?.recyclerViewGroups

                    recyclerView?.layoutManager =
                        LinearLayoutManager(context, RecyclerView.VERTICAL, false)
                    val dataList = ArrayList<GroupDetail>()

                    for (indice in 0 until response.length()) {
                        val group: GroupDetail = Json.nonstrict.parse(
                            GroupDetail.serializer(),
                            response.getJSONObject(indice).toString()
                        )
                        dataList.add(group)
                    }
                    val rvAdapter = RvAdapter(dataList) { group: GroupDetail -> partItemClicked(group) }
                    recyclerView?.adapter = rvAdapter
                },
                Response.ErrorListener { error ->
                    Log.d("groupsFragment", error.toString())
                })

            queue.add(request)
        }

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

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     *
     *
     * See the Android Training lesson [Communicating with Other Fragments]
     * (http://developer.android.com/training/basics/fragments/communicating.html)
     * for more information.
     */
    interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        fun onFragmentInteraction(uri: Uri)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment GroupsFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String = "", param2: String = "") =
            GroupsFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}
