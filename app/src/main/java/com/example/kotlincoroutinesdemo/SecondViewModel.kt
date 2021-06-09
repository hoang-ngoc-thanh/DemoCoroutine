package com.example.kotlincoroutinesdemo

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.kotlincoroutinesdemo.api.ApiHelper
import com.example.kotlincoroutinesdemo.base.BaseViewModel
import com.example.kotlincoroutinesdemo.model.User
import com.example.kotlincoroutinesdemo.response.ResultWrapper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SecondViewModel(private val apiHelper: ApiHelper) : BaseViewModel() {
    init {
        callApiUsers()
    }

    private val userLiveData = MutableLiveData<List<User>>()
    private val networkErrorLiveData = MutableLiveData<ResultWrapper.NetworkError>()
    private val genericErrorLiveData = MutableLiveData<ResultWrapper.GenericError>()

    internal fun getUsersLiveData() = userLiveData
    internal fun getNetworkErrorLiveData() = networkErrorLiveData
    internal fun getGenericErrorLiveData() = genericErrorLiveData

    private fun callApiUsers() {
        viewModelScope.launch {
            when (val response = safeCallApi { apiHelper.getUsers() }) {
                is ResultWrapper.Success -> {
                    postStateLoadingProgress(false)
                    userLiveData.postValue(response.value)
                }
                is ResultWrapper.NetworkError -> {
                    postStateLoadingProgress(false)
                    Log.d("mmm", "error network")
                }
                is ResultWrapper.GenericError -> {
                    Log.d("mmm", "${response.code} -- ${response.massage}")
                    postStateLoadingProgress(false)
                }
            }
        }
    }
}
