package com.example.myapplication.network

import com.example.myapplication.data.model.Contact
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface ContactApi {

    @POST("/contact")
    suspend fun saveContact(@Body contact: Contact): Response<Contact>
}