package com.example.myapplication

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable

@Serializable
@Entity
data class Contact(
    var name: String,
    var phoneNumber: String = ""
) {

    @PrimaryKey
    var contact: String = ""
    @Ignore
    var participate: Boolean = false
    @Ignore
    var permission: Int = 0
    @Ignore
    var checked: Boolean = false
}