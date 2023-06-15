package com.example.openinapp

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.commit
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.openinapp.Service.ApiService
import com.example.openinapp.Service.MainViewModel
import com.example.openinapp.Service.MyFactory
import com.example.openinapp.UI.MainFragment
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create

private const val TAG = "MainActivity"
class MainActivity : AppCompatActivity() {
    lateinit private var mViewModel:MainViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mViewModel = ViewModelProvider(this,MyFactory((application as MainApplication).repo)).get(MainViewModel::class.java)
        if(getSharedPreferences("MyPref",Context.MODE_PRIVATE).getString("token","").equals(""))
        getSharedPreferences("MyPref",Context.MODE_PRIVATE).edit().putString("token","Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpZCI6MjU5MjcsImlhdCI6MTY3NDU1MDQ1MH0.dCkW0ox8tbjJA2GgUx2UEwNlbTZ7Rr38PVFJevYcXFI").apply()

        val token = getSharedPreferences("MyPref",Context.MODE_PRIVATE).getString("token","")
        mViewModel.getResponse(token!!)
        supportFragmentManager.commit {
            add(R.id.container,MainFragment())
        }
    }
}