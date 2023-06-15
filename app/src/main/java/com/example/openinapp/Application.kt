package com.example.openinapp

import android.app.Application
import android.content.Context
import com.example.openinapp.Service.MainRepository
import com.example.openinapp.Service.RetrofitInstance

class MainApplication : Application(){
    lateinit var repo: MainRepository
    override fun onCreate() {
        super.onCreate()
        repo = MainRepository(RetrofitInstance.api)
    }
}