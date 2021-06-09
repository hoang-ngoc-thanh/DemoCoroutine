package com.example.kotlincoroutinesdemo.api

import com.example.kotlincoroutinesdemo.model.User
import com.example.kotlincoroutinesdemo.response.ResultWrapper
import retrofit2.http.GET

interface ApiService {
    @GET("users/ncnc")
    suspend fun getUser(): List<User>

    @GET("more-users")
    suspend fun getMoreUsers(): List<User>
}
