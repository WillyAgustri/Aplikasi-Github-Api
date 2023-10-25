package com.example.githubapplication.ui

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.githubapplication.data.api.ApiConfig
import com.example.githubapplication.data.database.FavoriteUserRepo
import com.example.githubapplication.data.database.UserEntity
import com.example.githubapplication.data.response.DetailResponse
import com.example.githubapplication.data.response.FollowResponseItem
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UserDetailViewModel(application: Application) : ViewModel() {
    private val userDetail = MutableLiveData<DetailResponse>()
    val getUserDetail: LiveData<DetailResponse> = userDetail

    private val followers = MutableLiveData<ArrayList<FollowResponseItem>>()
    val getFollowers: LiveData<ArrayList<FollowResponseItem>> = followers

    private val following = MutableLiveData<ArrayList<FollowResponseItem>>()
    val getFollowing: LiveData<ArrayList<FollowResponseItem>> = following

    private val isLoading = MutableLiveData<Boolean>()
    val getIsLoading: LiveData<Boolean> = isLoading

    private val detailUserData: FavoriteUserRepo = FavoriteUserRepo(application)

    fun insertDataUser(user: UserEntity) {
        detailUserData.insertUser(user)
    }

    fun deleteDataUser(user: UserEntity) {
        detailUserData.deleteUser(user)
    }



    fun getDataByUsername(username: String) = detailUserData.getDataByUsername(username)

    fun detailUser(username: String ) {
        isLoading.value = true
        val client = ApiConfig.getApiService().fetchUserDetails(username)
        client.enqueue(object : Callback<DetailResponse> {
            override fun onResponse(
                call: Call<DetailResponse>,
                response: Response<DetailResponse>)
            {
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
    fun getFollower(username: String ) {
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

    class ViewModelFactory(private val application: Application) : ViewModelProvider.NewInstanceFactory() {
        companion object {
            @Volatile
            private var INSTANCE: ViewModelFactory? = null

            @JvmStatic
            fun getInstance(application: Application): ViewModelFactory {
                if (INSTANCE == null) {
                    synchronized(ViewModelFactory::class.java) {
                        INSTANCE = ViewModelFactory(application)
                    }
                }
                return INSTANCE as ViewModelFactory
            }
        }

        @Suppress("UNCHECKED_CAST")
        override fun <view : ViewModel> create(modelClass: Class<view>): view {
            if (modelClass.isAssignableFrom(UserDetailViewModel::class.java)) {
                return UserDetailViewModel(application) as view
            }
            throw IllegalArgumentException("Unknown ViewModel Class: ${modelClass.name}")
        }
    }

    companion object {
        private const val TAG = "UserDetailViewModel"
    }
}
