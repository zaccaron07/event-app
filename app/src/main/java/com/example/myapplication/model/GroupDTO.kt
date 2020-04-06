package com.example.myapplication.model

import kotlinx.serialization.Serializable

@Serializable
data class GroupDTO(
    var name: String,
    var description: String = "",
    var startTime: String = "",
    var date: String = ""
) {
    var id: String = ""
    var contacts: List<ContactTemp> = listOf()
}

@Serializable
data class ContactTemp(
    var participate: Boolean,
    var permission: Int
) {
    var id: String = ""
    lateinit var contact: ContactDetailTemp
}

@Serializable
data class ContactDetailTemp(
    var id: String,
    var name: String,
    var phoneNumber: String
)