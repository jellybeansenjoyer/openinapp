package com.example.openinapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.commit
import androidx.lifecycle.lifecycleScope
import com.example.openinapp.Service.ApiService
import com.example.openinapp.UI.MainFragment
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create

private const val TAG = "MainActivity"
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportFragmentManager.commit {
            add(R.id.container,MainFragment())
        }
        Retrofit.Builder().baseUrl("https://api.inopenapp.com/api/v1/dashboardNew/").addConverterFactory(GsonConverterFactory.create()).build().create<ApiService>().apply {
            lifecycleScope.launch {
                val ans = this@apply.call("Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpZCI6MjU5MjcsImlhdCI6MTY3NDU1MDQ1MH0.dCkW0ox8tbjJA2GgUx2UEwNlbTZ7Rr38PVFJevYcXFI")
                Log.e(TAG,ans.toString())
            }
        }
    }
}