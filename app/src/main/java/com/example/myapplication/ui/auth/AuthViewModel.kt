package com.example.myapplication.ui.auth

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.myapplication.R
import com.example.myapplication.base.BaseViewModel
import com.example.myapplication.data.model.Contact
import com.example.myapplication.data.repositories.ContactRepository
import com.example.myapplication.utils.Coroutines

class AuthViewModel(
    private val contactRepository: ContactRepository
) : BaseViewModel() {

    private val _userCreatedAndAuthenticated = MutableLiveData<Boolean>()
    val userCreatedAndAuthenticated: LiveData<Boolean>
        get() = _userCreatedAndAuthenticated

    enum class AuthenticationState {
        UNAUTHENTICATED,
        AUTHENTICATED,
        INVALID_AUTHENTICATION
    }

    init {
        verifyUserCreatedAndAuthenticated()
    }

    var authenticationState =
        if (getFirebaseAuth().currentUser != null) AuthenticationState.AUTHENTICATED else AuthenticationState.UNAUTHENTICATED

    fun insertContact(contact: Contact) = contactRepository.insertContact(contact)
    fun getContact() = contactRepository.getContact()
    fun deleteContact() = contactRepository.deleteContact()
    fun getFirebaseAuth() = contactRepository.getFirebaseAuth()

    private fun verifyUserCreatedAndAuthenticated() {
        Coroutines.ioThenMain(
            { contactRepository.getContact() },
            {
                if (!it?.id.isNullOrEmpty() && authenticationState == AuthenticationState.AUTHENTICATED) {
                    navigate(R.id.action_splashFragment_to_groupsFragment)
                } else {
                    navigate(R.id.action_splashFragment_to_loginFragment)
                }
            }
        )
    }
}