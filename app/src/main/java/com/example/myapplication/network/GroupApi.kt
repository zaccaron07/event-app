package com.example.myapplication.network

import com.example.myapplication.data.model.GroupDTO
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface GroupApi {

    @GET("/contact/{id}/group")
    suspend fun getGroups(@Path("id") id: String): Response<List<GroupDTO>>
}