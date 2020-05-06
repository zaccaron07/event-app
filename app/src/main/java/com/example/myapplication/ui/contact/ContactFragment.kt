package com.example.myapplication.ui.contact

import android.Manifest
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.telephony.TelephonyManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat.checkSelfPermission
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.data.model.Contact
import com.example.myapplication.databinding.FragmentContactBinding
import com.example.myapplication.ui.group.GroupsViewModel
import com.example.myapplication.utils.extension.kodeinViewModel
import kotlinx.android.synthetic.main.fragment_contact.*
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.kodein
import java.util.*

class ContactFragment : Fragment(), KodeinAware, RecyclerViewContactClickListener {

    override val kodein by kodein()

    private val viewModel: ContactViewModel by kodeinViewModel()
    private val viewModelGroups: GroupsViewModel by kodeinViewModel()
    private lateinit var binding: FragmentContactBinding
    private val REQUEST_READ_CONTACTS = 100

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_contact, container, false)

        viewModel.groupSaved.observe(viewLifecycleOwner, Observer {
            if (it) {
                findNavController().navigate(R.id.action_contactFragment_to_groupsFragment)
            }
        })

        viewModel.countryIso =
            (requireContext().getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager).networkCountryIso.toUpperCase(
                Locale.getDefault()
            )
        viewModel.group = viewModelGroups.group

        viewModel.addMyContactToGroup()

        viewModel.contacts.observe(viewLifecycleOwner, Observer {
            if (!it.isNullOrEmpty()) {
                initializeListView(it)
            }
        })

        binding.viewmodel = viewModel

        if (this.checkContactPermission()) {
            viewModel.loadContacts()
        }

        return binding.root
    }

    private fun checkContactPermission(): Boolean {
        var permissionGranted = false

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && checkSelfPermission(
                requireContext(),
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
        rvContactDetail.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)

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
                    context,
                    "Sem a permissão dos contatos não será possivel criar adicionar pessoas ao grupo!",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }

    private fun messageContactWithNoId() {
        val builder: AlertDialog.Builder = AlertDialog.Builder(context)

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
}
