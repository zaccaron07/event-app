package com.example.myapplication.ui.auth

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.example.myapplication.base.BaseViewModel
import com.example.myapplication.data.model.Contact
import com.example.myapplication.data.repositories.ContactRepository

class AuthViewModel(
    private val contactRepository: ContactRepository
) : BaseViewModel() {
    enum class AuthenticationState {
        UNAUTHENTICATED,
        AUTHENTICATED,
        INVALID_AUTHENTICATION
    }

    var authenticationState =
        if (getFirebaseAuth().currentUser != null) AuthenticationState.AUTHENTICATED else AuthenticationState.UNAUTHENTICATED

    fun insertContact(contact: Contact) = contactRepository.insertContact(contact)
    fun getContact() = contactRepository.getContact()
    fun deleteContact() = contactRepository.deleteContact()
    fun getFirebaseAuth() = contactRepository.getFirebaseAuth()
}