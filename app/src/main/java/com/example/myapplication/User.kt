package com.example.myapplication

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable
import java.util.*
import kotlin.collections.ArrayList

@Serializable
@Entity
data class User(
    var name: String,
    var phoneNumber: String = ""
) {

    @PrimaryKey
    var id: String = UUID.randomUUID().toString()

    @Ignore
    var groups: ArrayList<UserGroups> = ArrayList()
}

@Serializable
data class UserGroups(var group: String = "") {
    var participate: Boolean = false
    var permision: Long = 1
}