package com.example.myapplication.ui.contact

import androidx.lifecycle.MutableLiveData
import com.example.myapplication.base.BaseViewModel
import com.example.myapplication.data.model.Contact
import com.example.myapplication.data.model.Group
import com.example.myapplication.data.repositories.ContactRepository
import com.example.myapplication.data.repositories.GroupRepository
import com.example.myapplication.utils.Coroutines
import com.google.i18n.phonenumbers.PhoneNumberUtil
import kotlinx.coroutines.Job

class ContactViewModel(
    private val groupRepository: GroupRepository,
    private val contactRepository: ContactRepository
) : BaseViewModel() {

    var group = Group()
    var groupSaved = MutableLiveData<Boolean>()
    lateinit var _contacts: MutableList<Contact>

    val contacts = MutableLiveData<MutableList<Contact>>()
    lateinit var countryIso: String

    private lateinit var job: Job
    private lateinit var jobContactsWithLogin: Job

    private fun getCurrentContact() = contactRepository.getContact()

    fun saveGroup() {
        job = Coroutines.ioThenMain(
            { groupRepository.saveGroup(group) },
            { groupSaved.value = true }
        )
    }

    private fun getContactsWithLogin() {
        var phoneNumbers = ""
        _contacts.forEach {
            val phoneNumber = it.phoneNumber

            phoneNumbers += "$phoneNumber,"
        }

        phoneNumbers = phoneNumbers.substring(0, phoneNumbers.length - 1)

        jobContactsWithLogin = Coroutines.ioThenMain(
            { contactRepository.getContactsWithLogin(phoneNumbers) },
            { onGetContactsWithLoginSuccess(it) }
        )
    }

    private fun onGetContactsWithLoginSuccess(contacts: MutableList<Contact>?) {
        if (!contacts.isNullOrEmpty()) {
            contacts.forEach { contact ->
                _contacts.find { it.phoneNumber == contact.phoneNumber }?.id =
                    contact.id
            }

            _contacts.sortBy { contact -> contact.name }

            this.contacts.value = _contacts
        }
    }

    fun loadContacts() {
        _contacts = contactRepository.getContactsFromPhone()

        formatContacts()
        getContactsWithLogin()
    }

    fun addMyContactToGroup() {
        Coroutines.io {
            val myContact = getCurrentContact()

            myContact.contact = myContact.id

            group.contacts.add(myContact)
        }
    }

    private fun formatContacts() {
        _contacts.forEach {
            if (it.phoneNumber.length >= 8) {
                it.phoneNumber = formatNumber(it.phoneNumber)
            }
        }
    }

    private fun formatNumber(phoneNumber: String): String {
        var newPhoneNumber = phoneNumber

        try {
            newPhoneNumber = if (newPhoneNumber.startsWith(
                    "+",
                    true
                )
            ) newPhoneNumber else getIsoPrefix() + newPhoneNumber

            val phoneNumberFormat = PhoneNumberUtil.getInstance().parse(newPhoneNumber, "BR")

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

    private fun getIsoPrefix(): String {
        return "+${PhoneNumberUtil.getInstance().getCountryCodeForRegion(countryIso)}"
    }

    private fun addStateAreaCode(phoneNumber: String): String {
        return "${this.getCurrentContact().phoneNumber.substring(3, 5)}$phoneNumber"
    }

    override fun onCleared() {
        super.onCleared()

        if (::job.isInitialized) job.cancel()
        if (::jobContactsWithLogin.isInitialized) jobContactsWithLogin.cancel()
    }
}
