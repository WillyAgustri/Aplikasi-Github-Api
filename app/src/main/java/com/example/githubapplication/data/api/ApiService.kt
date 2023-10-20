package com.example.githubapplication.data.api

import com.example.githubapplication.data.response.DetailResponse
import com.example.githubapplication.data.response.FollowResponseItem
import com.example.githubapplication.data.response.SearchResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {

    @GET("search/users")
    fun fetchUserSearchResults(
        @Query("q") username: String
    ): Call<SearchResponse>

    @GET("users/{username}")
    fun fetchUserDetails(
        @Path("username") username: String
    ): Call <DetailResponse>

    @GET("users/{username}/followers")
    fun fetchUserFollower(
        @Path("username") username: String
    ): Call<ArrayList<FollowResponseItem>>

    @GET("users/{username}/following")
    fun fetchUserFollowing(
        @Path("username") username : String
    ):  Call <ArrayList<FollowResponseItem>>
}