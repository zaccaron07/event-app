package com.example.myapplication.data.repositories

import android.content.ContentResolver
import android.provider.ContactsContract
import com.example.myapplication.data.model.Contact
import com.example.myapplication.data.database.AppDatabase
import com.example.myapplication.network.ContactApi
import com.google.firebase.auth.FirebaseAuth

class ContactRepository(
    private val contactApi: ContactApi,
    private val db: AppDatabase,
    private val contentResolver: ContentResolver
) : SafeApiRequest() {

    fun getFirebaseAuth() = FirebaseAuth.getInstance()

    fun getContact() = db.contactDao().getContact()
    suspend fun saveContact(contact: Contact) = apiRequest { contactApi.saveContact(contact) }
    fun insertContact(contact: Contact) = db.contactDao().insertContact(contact)
    fun deleteContact() = db.contactDao().deleteContact()
    suspend fun getContactsWithLogin(phoneNumber: String) =
        apiRequest { contactApi.getContactsWithLogin(phoneNumber) }

    fun getContactsFromPhone(): MutableList<Contact> {
        val resolver: ContentResolver = contentResolver
        val contacts = mutableListOf<Contact>()
        val cursor = resolver.query(
            ContactsContract.Contacts.CONTENT_URI, null, null, null,
            null
        )

        if (cursor?.count!! > 0) {
            while (cursor.moveToNext()) {
                val id = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID))
                val name =
                    cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME))
                val phoneNumber = (cursor.getString(
                    cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER)
                )).toInt()

                if (phoneNumber > 0) {
                    val cursorPhone = contentResolver.query(
                        ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                        null,
                        ContactsContract.CommonDataKinds.Phone.CONTACT_ID + "=?",
                        arrayOf(id),
                        null
                    )

                    if (cursorPhone?.count!! > 0) {
                        cursorPhone.moveToNext()
                        val phoneNumValue = cursorPhone.getString(
                            cursorPhone.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)
                        )

                        if (phoneNumValue.length >= 8) {
                            if (phoneNumValue != "") {
                                val contact = Contact(name, phoneNumValue)

                                contacts.add(contact)
                            }
                        }
                    }
                    cursorPhone.close()
                }
            }
        }

        cursor.close()

        return contacts
    }
}