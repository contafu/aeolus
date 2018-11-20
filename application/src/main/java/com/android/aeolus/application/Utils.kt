package com.android.aeolus.application

import kotlinx.coroutines.*

object Utils {
    fun <T> request(block: suspend CoroutineScope.() -> T, uiBlock: (suspend (T) -> Unit)? = null): Deferred<T> {
        return GlobalScope.async(Dispatchers.IO) { block(this) }.apply {
            GlobalScope.launch(Dispatchers.Main) { uiBlock?.invoke(await()) }
        }
    }
}