package com.example.myapplication.data.repositories

import com.example.myapplication.data.model.Contact
import com.example.myapplication.data.database.AppDatabase
import com.example.myapplication.network.ContactApi
import com.google.firebase.auth.FirebaseAuth

class ContactRepository(
    private val contactApi: ContactApi,
    private val db: AppDatabase
) : SafeApiRequest() {

    fun getFirebaseAuth() = FirebaseAuth.getInstance()

    fun getContact() = db.contactDao().getContact()
    suspend fun saveContact(contact: Contact) = apiRequest { contactApi.saveContact(contact) }
    fun insertContact(contact: Contact) = db.contactDao().insertContact(contact)
    fun deleteContact() = db.contactDao().deleteContact()
}