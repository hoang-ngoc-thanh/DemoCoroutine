package com.example.kotlincoroutinesdemo

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.kotlincoroutinesdemo.api.ApiHelperImp
import com.example.kotlincoroutinesdemo.api.RetrofitBuilder
import com.example.kotlincoroutinesdemo.databinding.ActivitySecondBinding
import com.example.kotlincoroutinesdemo.model.User
import com.example.kotlincoroutinesdemo.utils.ViewModelFactory

class SecondActivity : AppCompatActivity() {

    private lateinit var adapter: SecondAdapter
    private lateinit var viewModel: SecondViewModel

    private lateinit var viewBinding: ActivitySecondBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = ActivitySecondBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)

        setupViewModel()
        setupUI()
        setupObserver()
    }

    private fun setupUI() {
        adapter = SecondAdapter(arrayListOf())
        viewBinding.recyclerView.apply {
            layoutManager = LinearLayoutManager(this@SecondActivity)
            adapter = this@SecondActivity.adapter
        }

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            viewBinding.toolbar.title = "Request Api"
        }
    }

    private fun setupViewModel() {
        viewModel = ViewModelProviders.of(
            this,
            ViewModelFactory(
                ApiHelperImp(RetrofitBuilder.getApiService())
            )
        ).get(SecondViewModel::class.java)
    }

    private fun setupObserver() {
        viewModel.run {
            getUsersLiveData().observe(this@SecondActivity, {
                renderList(it)
            })

            getNetworkErrorLiveData().observe(this@SecondActivity, {

            })

            getGenericErrorLiveData().observe(this@SecondActivity, {

            })

            getLoadingProgress().observe(this@SecondActivity, {
                viewBinding.progressBar.visibility = if (it) View.VISIBLE else View.GONE
            })
        }
    }

    private fun renderList(users: List<User>) {
        adapter.addData(users)
        adapter.notifyDataSetChanged()
    }
}
