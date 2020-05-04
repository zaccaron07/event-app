package com.example.myapplication.network

import com.example.myapplication.data.model.Group
import com.example.myapplication.data.model.GroupDTO
import retrofit2.Response
import retrofit2.http.*

interface GroupApi {

    @GET("/contact/{id}/group")
    suspend fun getGroups(@Path("id") id: String): Response<List<GroupDTO>>

    @PUT("/group")
    suspend fun updateGroupDetail(@Query("contactId") contactId: String, @Body() group: Group): Response<Any>

    @POST("/group")
    suspend fun saveGroup(@Body() group: Group): Response<Any>
}