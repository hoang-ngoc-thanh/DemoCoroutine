package com.example.kotlincoroutinesdemo

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.kotlincoroutinesdemo.api.ApiHelperImp
import com.example.kotlincoroutinesdemo.api.RetrofitBuilder
import com.example.kotlincoroutinesdemo.databinding.ActivityFlowBinding
import com.example.kotlincoroutinesdemo.model.User
import com.example.kotlincoroutinesdemo.response.ResultWrapper
import com.example.kotlincoroutinesdemo.utils.ViewModelFactory

class FlowActivity : AppCompatActivity() {
    private lateinit var binding: ActivityFlowBinding
    private lateinit var adapter: SecondAdapter
    private lateinit var viewModel: FlowViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFlowBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setUpView()
        setUpViewModel()
        setupObserver()
    }

    private fun setUpView() {
        adapter = SecondAdapter(arrayListOf())
        binding.apply {
            recyclerView.layoutManager = LinearLayoutManager(this@FlowActivity)
            binding.recyclerView.addItemDecoration(
                DividerItemDecoration(
                    recyclerView.context,
                    (recyclerView.layoutManager as LinearLayoutManager).orientation
                )
            )
            recyclerView.adapter = adapter
        }

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            binding.toolbar.title = "FLow Activity"
        }
    }

    private fun setUpViewModel() {
        viewModel = ViewModelProviders.of(
            this,
            ViewModelFactory(ApiHelperImp(RetrofitBuilder.getApiService()))
        ).get(FlowViewModel::class.java)
    }

    private fun setupObserver() {
        viewModel.apply {
            getUsersLiveData().observe(this@FlowActivity, {
                when (it) {
                    is ResultWrapper.Success -> {
                        renderList(it.value)
                    }
                    is ResultWrapper.NetworkError -> {
                        Log.d("mmm", "error network")
                    }
                    is ResultWrapper.GenericError -> {
                        Log.d("mmm", "${it.code} -- ${it.massage}")
                    }
                }
            })

            getLoadingLiveData().observe(this@FlowActivity, {
                showProgressBar(it)
            })
        }
    }

    private fun renderList(users: List<User>) {
        adapter.addData(users)
        adapter.notifyDataSetChanged()
    }

    private fun showProgressBar(isEnable: Boolean) {
        binding.progressBar.visibility = if (isEnable) View.VISIBLE else View.GONE
    }
}
