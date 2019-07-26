package com.olympians.aeolus.sample

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.olympians.aeolus.Aeolus

class HomeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById<View>(R.id.request).setOnClickListener {
            for (i in 0..49) {
                request(i.toString())
            }
        }
    }

    private fun request(tag: String) {
        Aeolus.Builder<Response>()
                .addOnStart {
                    Log.d("TAG", "start")
                }
//                .addOnStart(object : OnAeolusStart {
//                    override fun onStart() {
//                    }
//                })
                .addRequest(Request(tag))
                .addCallback { err, it ->
                    Log.d("TAG", "response")
                }
//                .addCallback(object : OnAeolusCallback<Response> {
//                    override fun onFailure(exception: AeolusException) {
//
//                    }
//
//                    override fun onSuccess(response: Response) {
//
//                    }
//                })
                .addOnEnd {
                    Log.d("TAG", "end")
                }
//                .addOnEnd(object : OnAeolusEnd {
//                    override fun onEnd() {
//
//                    }
//                })
                .build()
    }
}