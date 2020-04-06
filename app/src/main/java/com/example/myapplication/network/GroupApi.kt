package com.example.myapplication.network

import com.example.myapplication.model.GroupDTO
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Path

interface GroupApi {

    @GET("/contact/{id}/group")
    fun getGroups(@Path("id") id: String): Observable<List<GroupDTO>>
}