package com.example.myapplication.data.repositories

import com.example.myapplication.network.GroupApi

class GroupRepository(
    private val groupApi: GroupApi
) : SafeApiRequest() {

    suspend fun getGroups(id: String) = apiRequest { groupApi.getGroups(id) }
}