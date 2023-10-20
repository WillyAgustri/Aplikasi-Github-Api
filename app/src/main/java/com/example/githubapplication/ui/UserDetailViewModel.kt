package com.example.githubapplication.ui

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.githubapplication.data.api.ApiConfig
import com.example.githubapplication.data.response.DetailResponse
import com.example.githubapplication.data.response.FollowResponseItem
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UserDetailViewModel : ViewModel() {
    private val userDetail = MutableLiveData<DetailResponse>()
    val getUserDetail: LiveData<DetailResponse> = userDetail

    private val followers = MutableLiveData<ArrayList<FollowResponseItem>>()
    val getFollowers: LiveData<ArrayList<FollowResponseItem>> = followers

    private val following = MutableLiveData<ArrayList<FollowResponseItem>>()
    val getFollowing: LiveData<ArrayList<FollowResponseItem>> = following

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
        client.enqueue(object : Callback<ArrayList<FollowResponseItem>> {
            override fun onResponse(
                call: Call<ArrayList<FollowResponseItem>>,
                response: Response<ArrayList<FollowResponseItem>>
            ) {
                isLoading.value = false
                if (response.isSuccessful && response.body() != null) {
                    followers.value = response.body()
                }
            }

            override fun onFailure(call: Call<ArrayList<FollowResponseItem>>, t: Throwable) {
                isLoading.value = false
                Log.e("UserDetailViewModel", "onFailure: ${t.message.toString()}")
            }
        })
    }

    fun getFollowing(username: String) {
        isLoading.value = true
        val client = ApiConfig.getApiService().fetchUserFollowing(username)
        client.enqueue(object : Callback<ArrayList<FollowResponseItem>> {
            override fun onResponse(
                call: Call<ArrayList<FollowResponseItem>>,
                response: Response<ArrayList<FollowResponseItem>>
            ) {
                isLoading.value = false
                if (response.isSuccessful && response.body() != null) {
                    following.value = response.body()
                }
            }

            override fun onFailure(call: Call<ArrayList<FollowResponseItem>>, t: Throwable) {
                isLoading.value = false
                Log.e("UserDetailViewModel", "onFailure: ${t.message.toString()}")
            }
        })
    }
}
