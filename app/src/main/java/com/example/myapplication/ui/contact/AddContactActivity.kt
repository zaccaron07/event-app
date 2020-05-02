package com.example.myapplication.ui.contact

import android.Manifest
import android.app.AlertDialog
import android.content.ContentResolver
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.provider.ContactsContract
import android.telephony.TelephonyManager
import android.util.Log
import android.widget.CheckBox
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.myapplication.R
import com.example.myapplication.data.model.Contact
import com.example.myapplication.data.model.Group
import com.example.myapplication.ui.HomeActivity
import com.example.myapplication.utils.APIConstants
import com.example.myapplication.utils.Coroutines
import com.example.myapplication.utils.extension.kodeinViewModel
import com.google.i18n.phonenumbers.PhoneNumberUtil
import kotlinx.android.synthetic.main.activity_add_contact.*
import kotlinx.android.synthetic.main.content_add_contact.*
import kotlinx.serialization.builtins.list
import kotlinx.serialization.json.Json
import org.json.JSONArray
import org.json.JSONObject
import org.kodein.di.KodeinAware
import org.kodein.di.android.kodein

class AddContactActivity : AppCompatActivity(), KodeinAware {

    override val kodein by kodein()

    private val viewModel: ContactViewModel by kodeinViewModel()

    private var group: Group? = null
    private var contactList: ArrayList<Contact> = ArrayList()
    private var contactListWithId: ArrayList<Contact> = ArrayList()
    private val REQUEST_READ_CONTACTS = 100
    private var stateAreaCode: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_contact)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        group = intent.extras?.getString("group")?.let { Json.parse(Group.serializer(), it) }

        Coroutines.io {
            val myContact = viewModel.getCurrentContact()

            stateAreaCode = myContact?.phoneNumber?.substring(3, 5)

            loadContacts()
        }

        this.addMyContactToGroup()

        this.floatingActionButtonDone.setOnClickListener {
            saveGroup()
        }
    }

    private fun addMyContactToGroup() {
        Coroutines.io {
            val myContact = viewModel.getCurrentContact()

            myContact.contact = myContact.id

            group?.contacts?.add(myContact)
        }
    }

    private fun loadContacts() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && checkSelfPermission(
                Manifest.permission.READ_CONTACTS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            requestPermissions(
                arrayOf(Manifest.permission.READ_CONTACTS),
                REQUEST_READ_CONTACTS
            )
        } else {
            contactList = getContacts()
            getContactsWithLogin()
        }
    }

    private fun initializeListView() {
        val rvContactDetail = this.rvContactDetail
        rvContactDetail.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)

        val rvAdapter =
            ContactRvAdapter(contactList) { contactDetail: Contact, checkBoxContact: CheckBox ->
                partItemClicked(contactDetail, checkBoxContact)
            }
        rvContactDetail.adapter = rvAdapter
    }

    private fun partItemClicked(contact: Contact, checkBoxContact: CheckBox) {
        if (contact.id != "") {
            contact.checked = !contact.checked
            contact.contact = contact.id
            checkBoxContact.isChecked = contact.checked

            if (contact.checked) {
                this.group?.contacts?.add(contact)
            } else {
                this.group?.contacts?.remove(contact)
            }
        } else {
            this.messageContactWithNoId()
            checkBoxContact.isChecked = false
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == REQUEST_READ_CONTACTS) {
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

    private fun synchronizeContactsId() {
        this.contactListWithId.forEach { contact ->
            this.contactList.find { it.phoneNumber == contact.phoneNumber }?.id =
                contact.id
        }

        this.contactList.sortBy { contact -> contact.name }
    }

    private fun getContactsWithLogin() {
        val queue = Volley.newRequestQueue(this)
        var url = "${APIConstants.BASE_URL}/contact/numbers"

        var uriParam = "?phoneNumber="
        this.contactList.forEach {
            var phoneNumber = it.phoneNumber

            phoneNumber = phoneNumber.replace("+", "%2B")

            uriParam += "$phoneNumber,"
        }

        uriParam = uriParam.substring(0, uriParam.length - 1)
        url += uriParam

        val request = JsonArrayRequest(
            Request.Method.GET, url, null,
            Response.Listener { response ->
                if (response.length() > 0) {
                    initContactsIdList(response)
                    synchronizeContactsId()
                }
                this.initializeListView()
            },
            Response.ErrorListener { error ->
                Log.d("groupsFragment", error.toString())
            })

        queue.add(request)
    }

    private fun initContactsIdList(items: JSONArray) {

        this.contactListWithId = (Json.nonstrict.parse(
            Contact.serializer().list,
            items.toString()
        ) as ArrayList<Contact>)
    }

    private fun getContacts(): ArrayList<Contact> {
        val resolver: ContentResolver = contentResolver
        var contactsList: ArrayList<Contact> = ArrayList()
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
                        var phoneNumValue = cursorPhone.getString(
                            cursorPhone.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)
                        )

                        if (phoneNumValue.length >= 8) {
                            phoneNumValue = formatNumber(phoneNumValue)

                            if (phoneNumValue != "") {
                                val contact = Contact(name, phoneNumValue)

                                contactsList.add(contact)
                            }
                        }
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

    private fun messageContactWithNoId() {
        val builder: AlertDialog.Builder = AlertDialog.Builder(this)

        builder
            .setMessage("Este contato ainda não possui uma conta no AppEvento!")
            .setTitle("Atenção")
            .apply {
                setPositiveButton("Ok", DialogInterface.OnClickListener { dialog, id ->
                })
            }

        builder
            .create()
            .show()
    }

    private fun formatNumber(phoneNumber: String): String {
        var newPhoneNumber = phoneNumber

        try {
            newPhoneNumber = if (newPhoneNumber.startsWith(
                    "+",
                    true
                )
            ) newPhoneNumber else getIsoPrefix() + newPhoneNumber

            var phoneNumberFormat = PhoneNumberUtil.getInstance().parse(newPhoneNumber, "BR")

            var nationalPhoneNumber = phoneNumberFormat.nationalNumber.toString()

            if (phoneNumberFormat.countryCode.toString() == "55") {
                if (nationalPhoneNumber.length == 8) {
                    nationalPhoneNumber = "9$nationalPhoneNumber"
                }

                if (nationalPhoneNumber.length == 9) {
                    nationalPhoneNumber = addStateAreaCode(nationalPhoneNumber)
                }

                if (nationalPhoneNumber.length == 10) {
                    nationalPhoneNumber =
                        "${nationalPhoneNumber.substring(0, 2)}9${nationalPhoneNumber.substring(2)}"
                }
            }

            newPhoneNumber = "+${phoneNumberFormat.countryCode}$nationalPhoneNumber"
        } catch (ex: Exception) {
            newPhoneNumber = ""
        }

        return newPhoneNumber
    }

    private fun addStateAreaCode(phoneNumber: String): String {
        return "${this.stateAreaCode}$phoneNumber"
    }

    private fun removeNonDigit(phoneNumber: String): String {
        var newPhoneNumber = phoneNumber

        newPhoneNumber = newPhoneNumber.replace(" ", "")
            .replace("#", "")
            .replace("(", "")
            .replace(")", "")
            .replace("-", "")

        return newPhoneNumber
    }

    private fun getIsoPrefix(): String {
        val countryIso =
            (applicationContext.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager).networkCountryIso.toUpperCase()

        return "+${PhoneNumberUtil.getInstance().getCountryCodeForRegion(countryIso)}"
    }

    private fun saveGroup() {
        val queue = Volley.newRequestQueue(this)
        val url = "${APIConstants.BASE_URL}/group"

        val jsonData = Json.stringify(Group.serializer(), this.group!!)
        val jsonObject = JSONObject(jsonData)

        val jsonArrayRequest = JsonObjectRequest(
            Request.Method.POST, url, jsonObject,
            Response.Listener { response ->
                Log.d("AddContactActivityReq1", response.toString())
                goToHome()
            },
            Response.ErrorListener { error ->
                Log.d("AddContactActivityReq1", error.toString())
            })

        queue.add(jsonArrayRequest)
    }

    private fun goToHome() {
        val intent = Intent(this, HomeActivity::class.java)
        startActivity(intent)
    }
}
