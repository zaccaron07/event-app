package com.example.myapplication

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.fragment_user_profile.view.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class ContactProfileFragment : Fragment() {

    private var db: AppDatabase? = null
    private var listener: OnFragmentInteractionListener? = null
    private var contact: Contact? = null
    private val LOG_TAG = "ContactProfileFragment"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        getUser()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_user_profile, container, false)

        view.buttonSave.setOnClickListener {
            saveUser()
        }

        return view
    }

    private fun getUser() {
        GlobalScope.launch {
            contact = db?.contactDao()?.getContact()

            if (contact == null)
                contact = Contact("name")
        }
    }

    private fun saveUser() {
        /*contact?.name = editTextName.text.toString()

        val currentUser = FirebaseAuth.getInstance().currentUser;

        contact?.phoneNumber = currentUser?.phoneNumber!!

        val url = "${APIConstants.BASE_URL}/contact"

        val jsonData = Json.stringify(Contact.serializer(), contact!!)
        val jsonObject = JSONObject(jsonData)

        val queue = Volley.newRequestQueue(context)

        val jsonObjectRequest = JsonObjectRequest(
            Request.Method.POST, url, jsonObject,
            Response.Listener { response ->
                contact?.id = response.get("id").toString()
                GlobalScope.launch {
                    db?.contactDao()?.insertContact(contact!!)
                    (activity as HomeActivity).replaceFragment(GroupsFragment.newInstance())
                }
            },
            Response.ErrorListener { error ->
                Log.d(LOG_TAG, error.toString())
            })

        queue.add(jsonObjectRequest)*/
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnFragmentInteractionListener) {
            listener = context
        } else {
            throw RuntimeException(context.toString() + " must implement OnFragmentInteractionListener")
        }

        this.db = AppDatabase.invoke(context)
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
            ContactProfileFragment().apply {
                arguments = Bundle().apply {
                }
            }
    }
}
