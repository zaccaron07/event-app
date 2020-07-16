package com.example.myapplication.data.repositories

import com.example.myapplication.base.BaseRepository
import com.example.myapplication.data.model.Group
import com.example.myapplication.data.network.GroupApi

class GroupRepository(
    private val groupApi: GroupApi
) : BaseRepository() {

    suspend fun getGroups(id: String) = apiRequest { groupApi.getGroups(id) }
    suspend fun updateGroupDetail(contactId: String, group: Group) =
        apiRequest { groupApi.updateGroupDetail(contactId, group) }
    suspend fun saveGroup(group: Group) = apiRequest { groupApi.saveGroup(group) }
}