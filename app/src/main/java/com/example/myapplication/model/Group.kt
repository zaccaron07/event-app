package com.example.myapplication.model

import kotlinx.serialization.Serializable

@Serializable
data class Group(
    var name: String,
    var description: String = "",
    var startTime: String = "",
    var date: String = ""
) {
    var id: String = ""
    var contacts: List<Contact> = listOf()
}