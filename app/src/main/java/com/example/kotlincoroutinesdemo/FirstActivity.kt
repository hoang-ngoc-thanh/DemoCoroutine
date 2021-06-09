package com.example.kotlincoroutinesdemo

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.kotlincoroutinesdemo.databinding.ActivityFirstBinding
import kotlinx.coroutines.*

@SuppressLint("SetTextI18n")
class FirstActivity : AppCompatActivity() {
    private lateinit var viewBinding: ActivityFirstBinding

    private val listProgressView = mutableListOf<View>()
    private val listTextView = mutableListOf<View>()
    private var parentCoroutineScope = CoroutineScope(Dispatchers.Main)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = ActivityFirstBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)

        initViews()
        initListeners()
    }

    private fun initViews() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            viewBinding.toolbar.title = "First Activity"
        }

        listProgressView.apply {
            viewBinding.run {
                add(progress1)
                add(progress2)
                add(progress3)
            }
        }

        listTextView.apply {
            viewBinding.run {
                add(tvSpentTime1)
                add(tvSpentTime2)
                add(tvSpentTime3)
            }
        }
    }

    private fun initListeners() {
        viewBinding.rg.setOnCheckedChangeListener { _, id ->
            when (id) {
                R.id.rb1 -> {
                    runBlock()
                }
                R.id.rb2 -> {
                    runLaunch()
                }
                R.id.rb3 -> {
                    runLaunchException()
                }
            }
        }

        viewBinding.btnTask1.setOnClickListener {
            parentCoroutineScope.launch {
                runTask(0)
            }
        }

        viewBinding.btnTask2.setOnClickListener {
            parentCoroutineScope.launch {
                runTask(1)
            }
        }

        viewBinding.btnTask3.setOnClickListener {
            parentCoroutineScope.launch {
                runTask(2)
            }
        }

        viewBinding.btnWithContextTask.setOnClickListener {
            viewBinding.tvTimeWithContext.text = "Total: 0"
            parentCoroutineScope.launch {
                val currentMillis = System.currentTimeMillis()

                val result1 = withContext(Dispatchers.Main) { runTask(0) }
                val result2 = withContext(Dispatchers.Main) { runTask(1) }
                val result3 = withContext(Dispatchers.Main) { runTask(2) }

                viewBinding.tvTimeWithContext.text =
                    "Total: ${(System.currentTimeMillis() - currentMillis) / 1000}"
            }
        }

        viewBinding.btnAsyncTask.setOnClickListener {
            viewBinding.tvTimeAsync.text = "Total: 0"
            parentCoroutineScope.launch {
                val currentMillis = System.currentTimeMillis()

                val result1 = async(Dispatchers.Main) { runTask(0) }
                val result2 = async(Dispatchers.Main) { runTask(1) }
                val result3 = async(Dispatchers.Main) { runTask(2) }

                Log.d("mmm111", "${result1.await()} <-> ${result2.await()} <-> ${result3.await()}")
                viewBinding.tvTimeAsync.text =
                    "Total: ${(System.currentTimeMillis() - currentMillis) / 1000}"
            }
        }

        viewBinding.fab.setOnClickListener {
            val intent = Intent(this, SecondActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
        }
    }

    // blocking function
    private fun runBlock() {
        runBlocking {
            Log.d("mmm1:", "runTask")
            delay(3000)
            Log.d("mmm1:", "done")
        }
        Log.d("mmm1:", "next")
    }

    // non-blocking function
    private fun runLaunch() {
        var name = "Jerry"
        parentCoroutineScope.launch {
            delay(3000)
            name = "Tom"
        }

        GlobalScope.launch {
            delay(100)
            Log.d("mmm2:", name)
        }
    }

    val handler = CoroutineExceptionHandler { _, exception ->
        Log.d("mmmE", exception.message.toString())
    }

    private fun runLaunchException() {
        runBlocking {
            GlobalScope.launch(handler) {
                getInfo()
                getInfoMore()
//                try {
//                    getInfo()
//                } catch (e: Exception) {
//                    Log.d("mmmE11", e.message.toString())
//                }
//
//                try {
//                    getInfoMore()
//                } catch (e: Exception) {
//                    Log.d("mmmE12", e.message.toString())
//                }
            }
        }
    }

    private suspend fun runTask(type: Int): Any {
        (listTextView[type] as TextView).text = "St: 0"
        (listProgressView[type] as ProgressBar).visibility = View.VISIBLE
        val currentMillis = System.currentTimeMillis()
        val result: Any = when (type) {
            0 -> firstTask()
            1 -> secondTask()
            2 -> thirdTask()
            else -> {
                Any()
            }
        }
        (listTextView[type] as TextView).text =
            "St: ${(System.currentTimeMillis() - currentMillis) / 1000}"
        (listProgressView[type] as ProgressBar).visibility = View.INVISIBLE
        return result
    }

    private suspend fun firstTask(): String {
        delay(3000)
        return "Success";
    }

    private suspend fun secondTask(): Int {
        delay(9000)
        return 100;
    }

    private suspend fun thirdTask(): Float {
        delay(6000)
        return 7.0f;
    }

    private fun getInfo() {
        throw IndexOutOfBoundsException("Index Out Of Bounds Exception")
    }

    private fun getInfoMore() {
        throw NoSuchElementException("No Such Element Exception")
    }

    override fun onDestroy() {
        super.onDestroy()
        parentCoroutineScope.cancel()
    }
}
