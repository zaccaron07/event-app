package com.example.myapplication.utils

import com.example.myapplication.model.Contact
import com.example.myapplication.model.Group
import com.example.myapplication.model.GroupDTO

class GroupUtils {

    fun formatJsonGroup(groupListDTO: List<GroupDTO>): ArrayList<Group> {
        var groupList = ArrayList<Group>()

        groupListDTO.forEach {
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

    private fun formatContacts(groupTemp: GroupDTO): ArrayList<Contact> {
        var contactList = ArrayList<Contact>()

        groupTemp.contacts.forEach {
            var contact = Contact(
                it.contact.name,
                it.contact.phoneNumber
            )

            contact.id = it.contact.id
            contact.participate = it.participate
            contact.permission = it.permission

            contactList.add(contact)
        }

        return contactList
    }
}