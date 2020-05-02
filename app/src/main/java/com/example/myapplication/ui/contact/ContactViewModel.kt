package com.example.myapplication.ui.contact

import com.example.myapplication.base.BaseViewModel
import com.example.myapplication.data.repositories.ContactRepository

class ContactViewModel(
    private val contactRepository: ContactRepository
) : BaseViewModel() {

    fun getCurrentContact() = contactRepository.getContact()
}