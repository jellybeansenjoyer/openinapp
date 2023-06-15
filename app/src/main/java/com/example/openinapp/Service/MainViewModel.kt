package com.example.openinapp.Service

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.openinapp.Model.Links
import com.example.openinapp.Model.OverallUrlChart
import com.example.openinapp.Model.Response
import kotlinx.coroutines.launch
import java.lang.IllegalArgumentException
import kotlin.reflect.full.memberProperties

private const val TAG = "MainViewModel"
class MainViewModel(private val repository: MainRepository) : ViewModel() {
    private val _response: MutableLiveData<Response> = MutableLiveData()
    val response : LiveData<Response>
        get() =  _response

    private val _links: MutableLiveData<ArrayList<Links>> = MutableLiveData()
    val links : LiveData<ArrayList<Links>>
        get() =  _links

    private val _topLinks: MutableLiveData<ArrayList<Links>> = MutableLiveData()
    val topLinks : LiveData<ArrayList<Links>>
        get() =  _topLinks

    fun getResponse(token:String){
        viewModelScope.launch {
            _response.value = repository.getResponse(token)
            Log.e(TAG,_response.value.toString())
        }
    }
    fun getTopLinks():ArrayList<Links>{
        val topLinks = _response.value!!.data.top_links
        val topLinksModel = ArrayList<Links>()
        for(item in topLinks){
            topLinksModel.add(Links(item.original_image,item.total_clicks.toString(),item.created_at,item.title,item.web_link))
        }
        return topLinksModel
    }
    fun getLinks():ArrayList<Links>{
//        if(_response.value==null)
//                Log.e("abracadabra","null")
//        else
            Log.e("abracadabra",_response.value!!.toString())
        val response = _response.value!!
//        Log.e("abracadabra",response.data.toString())
        val recentLink = _response.value!!.data.recent_links
        val recentLinksModel = ArrayList<Links>()
        for(item in recentLink){
            recentLinksModel.add(Links(item.original_image,item.total_clicks.toString(),item.created_at,item.title,item.web_link))
        }
        return recentLinksModel
    }
    fun graphData():Map<String,String>{
        val graph = _response.value!!.data.overall_url_chart
        val map = HashMap<String,String>()
        for (property in OverallUrlChart::class.memberProperties) {
            map[property.name] = property.get(graph).toString()
        }
        return map
    }
}
class MyFactory(private val repository: MainRepository) : ViewModelProvider.Factory{
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.equals(MainViewModel::class.java)){
            return MainViewModel(repository) as T
        }

        throw IllegalArgumentException()
    }
}