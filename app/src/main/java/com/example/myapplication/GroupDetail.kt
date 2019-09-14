package com.example.myapplication

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable
import java.util.*

@Serializable
@Entity
data class GroupDetail(
    var name: String,
    var description: String = "",
    var startTime: String = "",
    var date: String = ""
) {

    @PrimaryKey
    var id: String = UUID.randomUUID().toString()
}