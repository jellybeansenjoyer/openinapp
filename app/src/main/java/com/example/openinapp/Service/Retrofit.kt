package com.example.openinapp.Service

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create

const val BASE_URL = "https://api.inopenapp.com/api/v1/"

object RetrofitInstance {
    val instance by lazy {
        Retrofit.Builder().baseUrl(BASE_URL).addConverterFactory(GsonConverterFactory.create()).build()
    }
    val api:ApiService by lazy{
        instance.create()
    }
}