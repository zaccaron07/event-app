package com.example.myapplication.ui.auth

import com.example.myapplication.base.BaseViewModel
import com.example.myapplication.data.model.Contact
import com.example.myapplication.data.repositories.ContactRepository

class AuthViewModel(
    private val contactRepository: ContactRepository
) : BaseViewModel() {
    fun insertContact(contact: Contact) = contactRepository.insertContact(contact)
    fun getContact() = contactRepository.getContact()
    fun deleteContact() = contactRepository.deleteContact()
    fun getFirebaseAuth() = contactRepository.getFirebaseAuth()
}