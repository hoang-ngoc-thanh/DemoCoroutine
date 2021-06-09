package com.example.kotlincoroutinesdemo.api

import com.example.kotlincoroutinesdemo.model.User
import com.example.kotlincoroutinesdemo.response.ResultWrapper
import kotlinx.coroutines.flow.Flow

interface ApiHelper {
    suspend fun getUsers(): List<User>

    fun getMoreUsers(): Flow<List<User>>
}
