package com.example.myapplication

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlinx.serialization.list

class GroupUtils {

    fun formatJsonGroup(jsonGroup: String): ArrayList<Group> {
        var groupList = ArrayList<Group>()
        var groupTemp: ArrayList<GroupTemp> =
            Json.nonstrict.parse(GroupTemp.serializer().list, jsonGroup) as ArrayList<GroupTemp>

        groupTemp.forEach {
            var group = Group(
                it.name,
                it.description,
                it.startTime,
                it.date
            )

            group.id = it.id
            group.contacts = formatContacts(it)

            groupList.add(group)
        }

        return groupList
    }

    private fun formatContacts(groupTemp: GroupTemp): ArrayList<Contact> {
        var contactList = ArrayList<Contact>()

        groupTemp.contacts.forEach {
            var contact = Contact(it.contact.name, it.contact.phoneNumber)

            contact.id = it.contact.id
            contact.participate = it.participate
            contact.permission = it.permission

            contactList.add(contact)
        }

        return contactList
    }

    @Serializable
    private data class GroupTemp(
        var name: String,
        var description: String = "",
        var startTime: String = "",
        var date: String = ""
    ) {
        var id: String = ""
        var contacts: ArrayList<ContactTemp> = ArrayList()
    }

    @Serializable
    private data class ContactTemp(
        var participate: Boolean,
        var permission: Int
    ) {
        var id: String = ""
        lateinit var contact: ContactDetailTemp
    }

    @Serializable
    private data class ContactDetailTemp(
        var id: String,
        var name: String,
        var phoneNumber: String
    )
}