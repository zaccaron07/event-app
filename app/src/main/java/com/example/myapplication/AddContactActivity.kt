package com.example.myapplication

import android.Manifest
import android.content.ContentResolver
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.provider.ContactsContract
import android.util.Log
import android.widget.CheckBox
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import kotlinx.android.synthetic.main.activity_add_contact.*
import kotlinx.android.synthetic.main.content_add_contact.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import org.json.JSONObject

class AddContactActivity : AppCompatActivity() {

    private var group: GroupDetail? = null
    private var contactDetaillist: ArrayList<ContactDetail> = ArrayList()
    private val PERMISSIONS_REQUEST_READ_CONTACTS = 100
    private var db: AppDatabase? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_contact)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        db = AppDatabase.invoke(this)

        group = intent.extras?.getString("group")?.let { Json.parse(GroupDetail.serializer(), it) }
        loadContacts()

        this.floatingActionButtonDone.setOnClickListener {
            saveGroup()
        }
    }

    private fun loadContacts() {
        var contactList: ArrayList<ContactDetail>

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && checkSelfPermission(
                Manifest.permission.READ_CONTACTS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            requestPermissions(
                arrayOf(Manifest.permission.READ_CONTACTS),
                PERMISSIONS_REQUEST_READ_CONTACTS
            )
        } else {
            contactList = getContacts()

            val rvContactDetail = this.rvContactDetail
            rvContactDetail.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)

            val rvAdapter =
                ContactRvAdapter(contactList) { contactDetail: ContactDetail, checkBoxContact: CheckBox ->
                    partItemClicked(contactDetail, checkBoxContact)
                }
            rvContactDetail.adapter = rvAdapter
            Log.d("AddContactActivity", contactList.size.toString())
        }
    }

    private fun partItemClicked(contactDetail: ContactDetail, checkBoxContact: CheckBox) {
        contactDetail.checked = !contactDetail.checked
        checkBoxContact.isChecked = contactDetail.checked

        if (contactDetail.checked) {
            this.contactDetaillist.add(contactDetail)
        } else {
            this.contactDetaillist.remove(contactDetail)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == PERMISSIONS_REQUEST_READ_CONTACTS) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                loadContacts()
            } else {
                Toast.makeText(
                    this,
                    "Permission must be granted in order to display contacts information",
                    Toast.LENGTH_LONG
                )
            }
        }
    }

    private fun getContacts(): ArrayList<ContactDetail> {
        val resolver: ContentResolver = contentResolver;
        var contactsList: ArrayList<ContactDetail> = ArrayList()
        val cursor = resolver.query(
            ContactsContract.Contacts.CONTENT_URI, null, null, null,
            null
        )

        if (cursor?.count!! > 0) {

            while (cursor.moveToNext()) {
                val id = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID))
                val name =
                    cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME))
                val phoneNumber = (cursor.getString(
                    cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER)
                )).toInt()

                if (phoneNumber > 0) {
                    val cursorPhone = contentResolver.query(
                        ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                        null,
                        ContactsContract.CommonDataKinds.Phone.CONTACT_ID + "=?",
                        arrayOf(id),
                        null
                    )

                    if (cursorPhone?.count!! > 0) {
                        cursorPhone.moveToNext()
                        val phoneNumValue = cursorPhone.getString(
                            cursorPhone.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)
                        )
                        val contact = ContactDetail(id, name, phoneNumValue)

                        contactsList.add(contact)
                    }
                    cursorPhone.close()
                }
            }
        } else {
            Toast.makeText(this, "Nenhum contato encontrado!", Toast.LENGTH_SHORT)
        }

        cursor.close()
        return contactsList
    }

    private fun saveGroup() {
        val queue = Volley.newRequestQueue(this)
        val url = "http://192.168.15.11:3000/group"

        val group = GroupDetail(
            this.group?.name.toString(),
            this.group?.description.toString(),
            this.group?.startTime.toString(),
            this.group?.date.toString()
        )

        val jsonData = Json.stringify(GroupDetail.serializer(), group)
        val jsonObject = JSONObject(jsonData)
        Log.d("AddContactActivity", jsonObject.toString())
        val jsonArrayRequest = JsonObjectRequest(
            Request.Method.POST, url, jsonObject,
            Response.Listener { response ->
                var insertedIds: String = "insertedIds"
                var jsonObject = JSONObject(response.getJSONArray(insertedIds).get(0).toString())
                Log.d("AddContactActivityReq1", jsonObject.getString("_id"))
                saveGroupUser(jsonObject.getString("_id"))
            },
            Response.ErrorListener { error ->
                Log.d("AddContactActivityReq1", error.toString())
            })

        queue.add(jsonArrayRequest)
    }

    private fun saveGroupUser(aGroupId: String) {
        val queue = Volley.newRequestQueue(this)
        var url = "http://192.168.15.11:3000/contactUser/"

        GlobalScope.launch {
            val user = db?.userDao()?.getUser()
            var userDetail = ContactDetail("", user!!.name, user!!.phoneNumber)
            val group = UserGroups(aGroupId)

            contactDetaillist.add(userDetail)

            group.participate = true
            group.permision = 1

            user?.groups?.add(group)
            val jsonData = Json.stringify(User.serializer(), user!!)
            val jsonObject = JSONObject(jsonData)
            Log.d("AddContactActivity", jsonObject.toString())

            url += user?.phoneNumber

            val jsonArrayRequest = JsonObjectRequest(
                Request.Method.POST, url, jsonObject,
                Response.Listener { response ->
                    Log.d("AddContactActivityReq2", response.toString())

                    goToHome()
                },
                Response.ErrorListener { error ->
                    Log.d("AddContactActivityReq2", error.toString())
                })

            queue.add(jsonArrayRequest)
        }
    }

    private fun goToHome() {
        val intent = Intent(this, HomeActivity::class.java)
        startActivity(intent)
    }
}

data class ContactDetail(
    var id: String,
    var name: String,
    var phoneNumber: String,
    var checked: Boolean = false
)
