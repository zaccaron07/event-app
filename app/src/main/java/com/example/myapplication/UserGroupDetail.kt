package com.example.myapplication

import kotlinx.serialization.Serializable

@Serializable
data class UserGroupDetail(
    var id: String = "",
    var name: String = "",
    var groupId: String = "",
    var participate: Boolean = false,
    var permission: Int = 0
) {
}