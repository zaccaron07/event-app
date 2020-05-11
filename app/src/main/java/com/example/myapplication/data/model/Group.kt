package com.example.myapplication.data.model

import kotlinx.serialization.Serializable

@Serializable
data class Group(
    var name: String = "",
    var description: String = "",
    var startTime: String = "",
    var date: String = ""
) {
    var id: String = ""
    var contacts: MutableList<Contact> = mutableListOf()
    var location: Location = Location()
}