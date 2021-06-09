package com.example.kotlincoroutinesdemo.api

import com.example.kotlincoroutinesdemo.model.User
import com.example.kotlincoroutinesdemo.response.ResultWrapper
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class ApiHelperImp(private val apiService: ApiService) : ApiHelper {
    override suspend fun getUsers(): List<User> = apiService.getUser()

    override fun getMoreUsers(): Flow<List<User>> = flow {
        emit(apiService.getMoreUsers())
    }
}
