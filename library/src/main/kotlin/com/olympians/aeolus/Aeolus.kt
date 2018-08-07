package com.olympians.aeolus

import android.os.Bundle
import android.os.Handler
import android.os.Message
import com.alibaba.fastjson.JSON
import com.olympians.aeolus.callback.OnAeolusCallback
import com.olympians.aeolus.callback.OnAeolusEnd
import com.olympians.aeolus.callback.OnAeolusStart
import com.olympians.aeolus.config.AeolusConfig
import com.olympians.aeolus.exception.AeolusException
import com.olympians.aeolus.utils.AeolusTools
import com.olympians.aeolus.utils.AnnotationTools
import okhttp3.OkHttpClient
import okhttp3.Response
import java.lang.reflect.ParameterizedType
import java.net.SocketTimeoutException
import java.util.concurrent.TimeUnit

object Aeolus {

    private const val RESPONSE_BODY = "BODY"
    private const val RESPONSE_CODE = "CODE"
    const val AEOLUS_CODE_OK = 0x00
    const val AEOLUS_CODE_JSON_ERROR = 0x01
    const val AEOLUS_CODE_SOCKET_ERROR = 0x02
    const val AEOLUS_CODE_INTERNAL_ERROR = 0x03

    private val client = AeolusConfig.getHttpClient().let {
        it ?: AeolusConfig.getHostnameVerifier().let {
            val client = OkHttpClient
                    .Builder()
                    .readTimeout(30, TimeUnit.SECONDS)
                    .writeTimeout(30, TimeUnit.SECONDS)
                    .connectTimeout(30, TimeUnit.SECONDS)
            if (null != it) {
                client.hostnameVerifier(it)
            }
            client.build()
        }
    }

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

                var response: Response? = null
                try {
                    response = client.newCall(request).execute()

                    if (response.isSuccessful) {
                        val code = response.code()
                        var bodyString = response.body()?.string()

                        val filter = AeolusConfig.getFilter()
                        if (null != filter) {
                            bodyString = filter.filter(bodyString)
                        }

                        sendMessage(Message().apply {
                            what = 0
                            data = Bundle().apply {
                                putInt(RESPONSE_CODE, code)
                                putString(RESPONSE_BODY, bodyString)
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
                    sendMessage(Message().apply {
                        what = 2
                        data = Bundle().apply {
                            putString(RESPONSE_BODY, e.localizedMessage)
                        }
                    })
                } finally {
                    try {
                        response?.close()
                    } catch (e: NoSuchMethodError) {
                        e.printStackTrace()
                    }
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
                                    callback?.onFailure(AeolusException(code = AEOLUS_CODE_JSON_ERROR, message = e.localizedMessage))
                                }
                            } else {
                                callback?.onFailure(AeolusException(code = AEOLUS_CODE_INTERNAL_ERROR, message = "type is not ParameterizedType"))
                            }
                        } else {
                            callback?.onFailure(AeolusException(code = code))
                        }
                    }
                }
                1 -> {
                    with(msg.data) {
                        val errMsg = getString(RESPONSE_BODY)
                        callback?.onFailure(AeolusException(code = AEOLUS_CODE_JSON_ERROR, message = errMsg))
                    }
                }
                2 -> {
                    with(msg.data) {
                        val errMsg = getString(RESPONSE_BODY)
                        callback?.onFailure(AeolusException(code = AEOLUS_CODE_SOCKET_ERROR, message = errMsg))
                    }
                }
            }
            end?.onEnd()
        }
    }
}