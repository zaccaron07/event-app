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
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.base.BaseFragment
import com.example.myapplication.data.model.Contact
import com.example.myapplication.databinding.FragmentContactBinding
import com.example.myapplication.ui.group.GroupsViewModel
import com.example.myapplication.utils.extension.kodeinViewModel
import kotlinx.android.synthetic.main.fragment_contact.*
import org.kodein.di.DIAware
import org.kodein.di.android.x.di
import java.util.*

class ContactFragment : BaseFragment(), DIAware, RecyclerViewContactClickListener {

    override val di by di()

    override val _viewModel: ContactViewModel by kodeinViewModel()
    private val viewModelGroups: GroupsViewModel by kodeinViewModel()
    private lateinit var binding: FragmentContactBinding
    private val REQUEST_READ_CONTACTS = 100

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_contact, container, false)

        _viewModel.countryIso =
            (requireContext().getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager).networkCountryIso.toUpperCase(
                Locale.getDefault()
            )
        _viewModel.group = viewModelGroups.group

        _viewModel.addMyContactToGroup()

        _viewModel.contacts.observe(viewLifecycleOwner, Observer {
            if (!it.isNullOrEmpty()) {
                initializeListView(it)
            }
        })

        binding.viewmodel = _viewModel

        if (this.checkContactPermission()) {
            _viewModel.loadContacts()
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
        if (contact.id.isNotEmpty()) {
            contact.checked = !contact.checked
            contact.contact = contact.id

            if (contact.checked) {
                _viewModel.group.contacts.add(contact)
            } else {
                _viewModel.group.contacts.remove(contact)
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
                _viewModel.loadContacts()
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
                setPositiveButton("Ok", DialogInterface.OnClickListener { _, _ ->
                })
            }

        builder
            .create()
            .show()
    }
}
