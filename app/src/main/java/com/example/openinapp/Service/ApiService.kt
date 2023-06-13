package com.example.openinapp.Service

import com.example.openinapp.Model.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers

interface ApiService {
    @GET("/")
    suspend fun call(@Header("Authorization") token:String): Response
}