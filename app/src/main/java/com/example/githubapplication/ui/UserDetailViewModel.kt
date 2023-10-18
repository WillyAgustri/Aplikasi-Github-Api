package com.example.githubapplication.ui

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.githubapplication.data.api.ApiConfig
import com.example.githubapplication.data.response.DetailResponse
import com.example.githubapplication.data.response.FollowerResponseItem
import com.example.githubapplication.data.response.FollowingResponseItem
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UserDetailViewModel : ViewModel() {
    private val userDetail = MutableLiveData<DetailResponse>()
    val getUserDetail: LiveData<DetailResponse> = userDetail

    private val followers = MutableLiveData<ArrayList<FollowerResponseItem>>()
    val getFollowers: LiveData<ArrayList<FollowerResponseItem>> = followers

    private val following = MutableLiveData<ArrayList<FollowingResponseItem>>()
    val getFollowing: LiveData<ArrayList<FollowingResponseItem>> = following

    private val isLoading = MutableLiveData<Boolean>()
    val getIsLoading: LiveData<Boolean> = isLoading

    fun detailUser(username: String) {
        isLoading.value = true
        val client = ApiConfig.getApiService().fetchUserDetails(username)
        client.enqueue(object : Callback<DetailResponse> {
            override fun onResponse(call: Call<DetailResponse>, response: Response<DetailResponse>) {
                isLoading.value = false
                if (response.isSuccessful) {
                    userDetail.value = response.body()
                }
            }

            override fun onFailure(call: Call<DetailResponse>, t: Throwable) {
                isLoading.value = false
                Log.e("UserDetailViewModel", "onFailure: ${t.message.toString()}")
            }
        })
    }

    fun getFollower(username: String) {
        isLoading.value = true
        val client = ApiConfig.getApiService().fetchUserFollower(username)
        client.enqueue(object : Callback<ArrayList<FollowerResponseItem>> {
            override fun onResponse(
                call: Call<ArrayList<FollowerResponseItem>>,
                response: Response<ArrayList<FollowerResponseItem>>
            ) {
                isLoading.value = false
                if (response.isSuccessful && response.body() != null) {
                    followers.value = response.body()
                }
            }

            override fun onFailure(call: Call<ArrayList<FollowerResponseItem>>, t: Throwable) {
                isLoading.value = false
                Log.e("UserDetailViewModel", "onFailure: ${t.message.toString()}")
            }
        })
    }

    fun getFollowing(username: String) {
        isLoading.value = true
        val client = ApiConfig.getApiService().fetchUserFollowing(username)
        client.enqueue(object : Callback<ArrayList<FollowingResponseItem>> {
            override fun onResponse(
                call: Call<ArrayList<FollowingResponseItem>>,
                response: Response<ArrayList<FollowingResponseItem>>
            ) {
                isLoading.value = false
                if (response.isSuccessful && response.body() != null) {
                    following.value = response.body()
                }
            }

            override fun onFailure(call: Call<ArrayList<FollowingResponseItem>>, t: Throwable) {
                isLoading.value = false
                Log.e("UserDetailViewModel", "onFailure: ${t.message.toString()}")
            }
        })
    }
}
