package com.example.friends.services

import com.example.friends.data.FriendData
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("/api/?")
    fun getFriendsInfo(@Query("results") friendCount: Int): Call<FriendData>
}