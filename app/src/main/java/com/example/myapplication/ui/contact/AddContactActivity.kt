package com.example.myapplication.ui.contact

import android.Manifest
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.telephony.TelephonyManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.data.model.Contact
import com.example.myapplication.data.model.Group
import com.example.myapplication.databinding.ActivityAddContactBinding
import com.example.myapplication.ui.HomeActivity
import com.example.myapplication.utils.extension.kodeinViewModel
import kotlinx.android.synthetic.main.activity_add_contact.*
import kotlinx.serialization.json.Json
import org.kodein.di.KodeinAware
import org.kodein.di.android.kodein
import java.util.*

class AddContactActivity : AppCompatActivity(), KodeinAware, RecyclerViewContactClickListener {

    override val kodein by kodein()

    private val viewModel: ContactViewModel by kodeinViewModel()

    private val REQUEST_READ_CONTACTS = 100
    private lateinit var binding: ActivityAddContactBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_add_contact)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        viewModel.groupSaved.observe(this, Observer { groupSaved ->
            if (groupSaved) {
                goToHome()
            }
        })

        viewModel.countryIso =
            (applicationContext.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager).networkCountryIso.toUpperCase(
                Locale.getDefault()
            )

        viewModel.group =
            intent.extras?.getString("group")?.let { Json.parse(Group.serializer(), it) }!!
        viewModel.addMyContactToGroup()

        viewModel.contacts.observe(this, Observer {
            if (!it.isNullOrEmpty()) {
                initializeListView(it)
            }
        })

        binding.viewmodel = viewModel

        if (this.checkContactPermission()) {
            viewModel.loadContacts()
        }
    }

    private fun checkContactPermission(): Boolean {
        var permissionGranted = false

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && checkSelfPermission(
                Manifest.permission.READ_CONTACTS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            requestPermissions(
                arrayOf(Manifest.permission.READ_CONTACTS),
                REQUEST_READ_CONTACTS
            )
        } else {
            permissionGranted = true
        }

        return permissionGranted
    }

    private fun initializeListView(contacts: MutableList<Contact>) {
        val rvContactDetail = this.rvContactDetail
        rvContactDetail.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)

        val rvAdapter = ContactAdapter(contacts, this)
        rvContactDetail.adapter = rvAdapter
    }

    override fun onRecyclerViewItemClick(contact: Contact) {
        if (contact.id != "") {
            contact.checked = !contact.checked
            contact.contact = contact.id

            if (contact.checked) {
                viewModel.group.contacts.add(contact)
            } else {
                viewModel.group.contacts.remove(contact)
            }
        } else {
            this.messageContactWithNoId()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == REQUEST_READ_CONTACTS) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                viewModel.loadContacts()
            } else {
                Toast.makeText(
                    this,
                    "Sem a permissão dos contatos não será possivel criar adicionar pessoas ao grupo!",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
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

    private fun goToHome() {
        val intent = Intent(this, HomeActivity::class.java)
        startActivity(intent)
    }
}
