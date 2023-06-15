package com.example.openinapp.Service

import com.example.openinapp.Model.Response

class MainRepository(private val apiService: ApiService) {
    suspend fun getResponse(token:String):Response{
        return apiService.call(token)
    }
}