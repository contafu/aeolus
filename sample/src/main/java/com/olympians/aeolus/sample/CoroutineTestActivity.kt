package com.olympians.aeolus.sample

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_coroutine_test.*
import kotlinx.coroutines.*
import okhttp3.OkHttpClient

class CoroutineTestActivity : AppCompatActivity(), CoroutineScope by MainScope() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_coroutine_test)

        val deferred1 = GlobalScope.async(Dispatchers.IO) {
            request1()
        }

        val deferred2 = GlobalScope.async(Dispatchers.IO) {
            request2()
        }

        GlobalScope.launch(Dispatchers.IO) {
            val string1 = deferred1.await()
            withContext(Dispatchers.Main) {
                textView1.text = String.format("%s", string1)
            }
            val string2 = deferred2.await()
            withContext(Dispatchers.Main) {
                textView2.text = String.format("%s", string2)
            }
        }

    }

    private suspend fun request1(): String {
        val client = OkHttpClient.Builder().build()
        val response = client.newCall(okhttp3.Request.Builder().url("http://192.168.31.150:3000/request1").build()).execute()

        val string = response.body?.string()

        return string ?: ""
    }

    private suspend fun request2(): String {
        val client = OkHttpClient.Builder().build()
        val response = client.newCall(okhttp3.Request.Builder().url("http://192.168.31.150:3000/request2").build()).execute()

        val string = response.body?.string()

        return string ?: ""
    }
}