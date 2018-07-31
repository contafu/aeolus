package com.olympians.aeolus

import android.os.Bundle
import android.os.Handler
import android.os.Message
import com.alibaba.fastjson.JSON
import com.olympians.aeolus.callback.OnAeolusCallback
import com.olympians.aeolus.callback.OnAeolusEnd
import com.olympians.aeolus.callback.OnAeolusStart
import com.olympians.aeolus.utils.AeolusTools
import com.olympians.aeolus.utils.AnnotationTools
import okhttp3.OkHttpClient
import java.lang.reflect.ParameterizedType
import java.net.SocketTimeoutException
import java.util.concurrent.TimeUnit

object Aeolus {

    private const val RESPONSE_BODY = "BODY"
    private const val RESPONSE_CODE = "CODE"
    const val AEOLUS_CODE_JSON_ERROR = 0x01

    private val client = OkHttpClient
            .Builder()
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .connectTimeout(30, TimeUnit.SECONDS)
            .build()

    class Builder<T> : Handler() {
        private var request: AeolusRequest? = null
        private var callback: OnAeolusCallback<T>? = null
        private var block: ((String, String?) -> Unit)? = null
        private var start: OnAeolusStart? = null
        private var end: OnAeolusEnd? = null

        fun addRequest(request: AeolusRequest): Builder<T> {
            this.request = request
            return this
        }

        fun addCallback(callback: OnAeolusCallback<T>): Builder<T> {
            this.callback = callback
            this.block = null
            return this
        }

        fun addOnStart(start: OnAeolusStart): Builder<T> {
            this.start = start
            return this
        }

        fun addOnEnd(end: OnAeolusEnd): Builder<T> {
            this.end = end
            return this
        }

        fun build() {
            start?.onStart()
            Thread(Runnable {
                val request = AeolusTools.buildRequest(AnnotationTools.extractParams(request))

                try {
                    val response = client.newCall(request).execute()

                    if (response.isSuccessful) {
                        val code = response.code()
                        val bodyString = response.body()?.string()
                        sendMessage(Message().apply {
                            what = 0
                            data = Bundle().apply {
                                putString(RESPONSE_BODY, bodyString)
                                putInt(RESPONSE_CODE, code)
                            }
                        })
                    } else {
                        val msg = response.message()
                        sendMessage(Message().apply {
                            what = 1
                            data = Bundle().apply {
                                putString(RESPONSE_BODY, msg)
                            }
                        })
                    }
                } catch (e: SocketTimeoutException) {
                    e.printStackTrace()
                }
            }).start()
        }

        override fun handleMessage(msg: Message?) {
            super.handleMessage(msg)
            when (msg?.what) {
                0 -> {
                    with(msg.data) {
                        val bodyString = getString(RESPONSE_BODY)
                        val code = getInt(RESPONSE_CODE)

                        if (200 == code) {
                            val types = callback?.javaClass?.genericInterfaces
                            val type = types?.get(0)
                            if (type is ParameterizedType) {
                                val argsTypes = type.actualTypeArguments
                                val argsType = argsTypes?.get(0)

                                try {
                                    val obj = JSON.parseObject<T>(bodyString, argsType)
                                    callback?.onSuccess(obj)
                                } catch (e: Exception) {
                                    callback?.onFailure(AEOLUS_CODE_JSON_ERROR, e.localizedMessage)
                                }
                            } else {
                                throw Exception("")
                            }
                        } else {
                            callback?.onFailure(code, null)
                        }
                    }
                }
                1 -> {
                    with(msg.data) {
                        val errMsg = getString(RESPONSE_BODY)
                        callback?.onFailure(AEOLUS_CODE_JSON_ERROR, errMsg)

                    }
                }
            }
            end?.onEnd()
        }
    }
}