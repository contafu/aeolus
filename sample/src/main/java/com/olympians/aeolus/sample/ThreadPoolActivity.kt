package com.olympians.aeolus.sample

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_thread_pool.*
import okhttp3.OkHttpClient
import okhttp3.Request
import java.util.concurrent.Executors

class ThreadPoolActivity : AppCompatActivity() {

    private val threadPool by lazy { Executors.newFixedThreadPool(1) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_thread_pool)

        submit.setOnClickListener {
            Log.d("TAG", "A")
            threadPool.execute {
                OkHttpClient.Builder().build().apply {
                    newCall(Request.Builder().url("http://192.168.31.150:3000/request1").build()).execute().apply {
                        val string = body?.string()
                        Log.d("TAG", string)
                    }
                }
            }
        }

        cancel.setOnClickListener {
            threadPool.shutdownNow()
        }
    }
}