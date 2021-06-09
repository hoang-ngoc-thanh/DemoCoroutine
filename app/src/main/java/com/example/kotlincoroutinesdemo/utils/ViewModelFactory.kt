package com.example.kotlincoroutinesdemo.utils

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.kotlincoroutinesdemo.FlowViewModel
import com.example.kotlincoroutinesdemo.SecondViewModel
import com.example.kotlincoroutinesdemo.api.ApiHelper

class ViewModelFactory(private val apiHelper: ApiHelper) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SecondViewModel::class.java)) {
            return SecondViewModel(apiHelper) as T
        }

        if (modelClass.isAssignableFrom(FlowViewModel::class.java)) {
            return FlowViewModel(apiHelper) as T
        }

        throw IllegalArgumentException("Unknown class name")
    }
}
