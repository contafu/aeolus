package com.android.aeolus.application

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.OnLifecycleEvent
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.*
import java.net.URL
import java.nio.charset.Charset

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        button.setOnClickListener {

        }
    }

    private fun request(address: String): String {
        val url = URL(address)
        val connection = url.openConnection()
        connection.connectTimeout = 30000
        connection.readTimeout = 30000
        connection.connect()

        val iStream = connection.getInputStream()
        val ba = iStream.readBytes()
        return String(ba, Charset.forName("UTF-8"))
    }

    class CoroutineLifecycleListener(private val deferred: Deferred<*>) : LifecycleObserver {
        @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
        fun cancelCoroutine() {
            if (!deferred.isCancelled) {
                deferred.cancel()
            }
        }
    }

    fun <T> LifecycleOwner.load(loader: () -> T): Deferred<T> {
        val deferred = GlobalScope.async(context = Dispatchers.IO, start = CoroutineStart.LAZY) {
            loader
        }
        lifecycle.addObserver(CoroutineLifecycleListener(deferred))
        return deferred
    }

    infix fun <T> Deferred<T>.then(block: (T) -> Unit): Job {
        return GlobalScope.launch(Dispatchers.Main) {
            block(this@then.await())
        }
    }

}
