package com.example.myapplication.ui.profile

import androidx.lifecycle.MutableLiveData
import com.example.myapplication.base.BaseViewModel
import com.example.myapplication.data.model.Contact
import com.example.myapplication.data.repositories.ContactRepository
import com.example.myapplication.utils.Coroutines

class ContactProfileViewModel(
    private val contactRepository: ContactRepository
) : BaseViewModel() {

    var contact = Contact("")
    var success = MutableLiveData<Boolean>()

    fun onSaveButtonClick() {
        val currentUser = contactRepository.getFirebaseAuth().currentUser

        contact.phoneNumber = currentUser?.phoneNumber!!

        Coroutines.main {
            contact = contactRepository.saveContact(contact)

            contactRepository.insertContact(contact)

            success.value = true
        }
    }
}