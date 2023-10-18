package com.example.githubapplication.ui

import android.service.controls.ControlsProviderService.TAG
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.githubapplication.data.api.ApiConfig
import com.example.githubapplication.data.response.ItemsItem
import com.example.githubapplication.data.response.SearchResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UserViewModel : ViewModel() {
    private val apiService = ApiConfig.getApiService()
    private val _userList = MutableLiveData<List<ItemsItem>>()
    val userList: LiveData<List<ItemsItem>> = _userList
    private val isLoading = MutableLiveData<Boolean>()
    val getIsLoading: LiveData<Boolean> = isLoading

    fun searchUsers(username: String) {
        try {

            isLoading.value = true
            apiService.fetchUserSearchResults(username).enqueue(object : Callback<SearchResponse> {
                override fun onResponse(
                    call: Call<SearchResponse>,
                    response: Response<SearchResponse>
                ) {
                    if (response.isSuccessful) {
                        isLoading.value = false
                        val searchResponse = response.body()
                        val users = searchResponse?.items
                        users?.let {
                            _userList.postValue(it as List<ItemsItem>?)
                        }
                    }
                }
                override fun onFailure(call: Call<SearchResponse>, t: Throwable) {
                    isLoading.value = false
                    Log.e(TAG, "onFailure: ${t.message.toString()}")
                }
            })

        } catch (e: Exception) {
            Log.d("Token e", e.toString())
        }

    }
}