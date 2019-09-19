package com.example.myapplication

import kotlinx.serialization.Serializable

@Serializable
data class Group(
    var name: String,
    var description: String = "",
    var startTime: String = "",
    var date: String = ""
) {
    var id: String = ""
    var contacts: ArrayList<Contact> = ArrayList()
}