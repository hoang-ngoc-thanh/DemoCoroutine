package com.example.kotlincoroutinesdemo

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.kotlincoroutinesdemo.api.ApiHelper
import com.example.kotlincoroutinesdemo.base.BaseViewModel
import com.example.kotlincoroutinesdemo.model.User
import com.example.kotlincoroutinesdemo.response.ResultWrapper
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

class FlowViewModel(private val apiHelper: ApiHelper) : BaseViewModel() {

    private val usersLiveData = MutableLiveData<ResultWrapper<List<User>>>()
    private val loadingLiveData = MutableLiveData<Boolean>()

    init {
        fetchUsers()
    }

    private fun fetchUsers() {
        viewModelScope.launch {
            loadingLiveData.postValue(true)
            apiHelper.getMoreUsers()
                .catch { e ->
                    loadingLiveData.postValue(false)
                    when (e) {
                        is IOException -> ResultWrapper.NetworkError
                        is HttpException -> {
                            val code = e.hashCode()
                            val message = e.message
                            ResultWrapper.GenericError(code, message)
                        }
                        else -> {
                            ResultWrapper.GenericError(null, null)
                        }
                    }
                }
                .collect {
                    loadingLiveData.postValue(false)
                    usersLiveData.postValue(ResultWrapper.Success(it))
                }
        }
    }

    internal fun getUsersLiveData() = usersLiveData
    internal fun getLoadingLiveData() = loadingLiveData
}
