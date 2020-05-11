package com.example.myapplication.data.model

import kotlinx.serialization.Serializable

@Serializable
data class Location(
    var address: String = "",
    var longitude: Double = 0.0,
    var latitude: Double = 0.0
)