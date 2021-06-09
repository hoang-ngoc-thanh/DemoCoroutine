package com.example.kotlincoroutinesdemo.base

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.kotlincoroutinesdemo.response.ResultWrapper
import retrofit2.HttpException
import java.io.IOException

open class BaseViewModel : ViewModel() {
    private val loadingProgress = MutableLiveData<Boolean>()

    internal fun getLoadingProgress(): MutableLiveData<Boolean> = loadingProgress

    internal fun postStateLoadingProgress(isLoading: Boolean) =
        loadingProgress.postValue(isLoading)

    open suspend fun <T> safeCallApi(apiCall: suspend () -> T): ResultWrapper<T> {
        postStateLoadingProgress(true)
        return try {
            ResultWrapper.Success(apiCall.invoke())
        } catch (throwable: Throwable) {
            when (throwable) {
                is IOException -> ResultWrapper.NetworkError
                is HttpException -> {
                    val code = throwable.hashCode()
                    val message = throwable.message
                    ResultWrapper.GenericError(code, message)
                }
                else -> {
                    ResultWrapper.GenericError(null, null)
                }
            }
        }
    }
}
