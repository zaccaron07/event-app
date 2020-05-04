package com.example.myapplication.network

import com.example.myapplication.data.model.Contact
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface ContactApi {

    @POST("/contact")
    suspend fun saveContact(@Body contact: Contact): Response<Contact>

    @GET("/contact/numbers")
    suspend fun getContactsWithLogin(@Query("phoneNumber") phoneNumber: String): Response<MutableList<Contact>>
}