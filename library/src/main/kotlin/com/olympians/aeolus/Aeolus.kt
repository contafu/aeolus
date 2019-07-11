package com.olympians.aeolus

import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.text.TextUtils
import com.alibaba.fastjson.JSON
import com.olympians.aeolus.callback.OnAeolusCallback
import com.olympians.aeolus.callback.OnAeolusEnd
import com.olympians.aeolus.callback.OnAeolusStart
import com.olympians.aeolus.config.AeolusConfig
import com.olympians.aeolus.config.AeolusConfig.TIMEOUT_CONFIG_TIME
import com.olympians.aeolus.config.AeolusConfig.TIMEOUT_CONFIG_UNIT
import com.olympians.aeolus.exception.AeolusException
import com.olympians.aeolus.utils.AeolusTools
import com.olympians.aeolus.utils.AnnotationTools
import okhttp3.OkHttpClient
import okhttp3.Response
import java.io.IOException
import java.lang.reflect.ParameterizedType
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import java.util.concurrent.Executor
import java.util.concurrent.Executors
import java.util.concurrent.ThreadPoolExecutor
import java.util.concurrent.TimeUnit

object Aeolus {

    private const val RESPONSE_CODE = "CODE"
    private const val RESPONSE_BODY = "BODY"

    const val AEOLUS_CODE_OK = 0x00
    /**
     * Aeolus 解析json异常
     */
    const val AEOLUS_CODE_JSON_ERROR = 0x01
    /**
     * 请求超时
     */
    const val AEOLUS_CODE_SOCKET_ERROR = 0x02
    /**
     * 连接超时
     */
    const val AEOLUS_CODE_CONNECT_ERROR = 0x03
    /**
     * Aeolus 内部异常
     */
    const val AEOLUS_CODE_INTERNAL_ERROR = 0x04
    /**
     * 业务异常
     */
    const val BUSINESS_EXCEPTION = 0x05
    /**
     * 域名解析异常
     */
    const val AEOLUS_CODE_UNKNOWN_HOSTNAME_ERROR = 0x06
    /**
     * 流操作异常
     */
    const val AEOLUS_CODE_IO_ERROR = 0x07

    private const val SocketTimeoutException_Code = -1
    private const val ConnectException = -2
    private const val UnknownHostException = -3
    private const val IOException = -4
    private const val Failure = 0
    private const val Success = 1

    private val client: OkHttpClient by lazy {
        AeolusConfig.getHttpClient() ?: OkHttpClient.Builder().also {
            val time: Long? = AeolusConfig.getTimeout()?.get(TIMEOUT_CONFIG_TIME) as Long?
            val unit: TimeUnit? = AeolusConfig.getTimeout()?.get(TIMEOUT_CONFIG_UNIT) as TimeUnit?
            if (null != time && 0L < time && null != unit) {
                it.readTimeout(time, unit)
                it.writeTimeout(time, unit)
                it.connectTimeout(time, unit)
            }

            val hostnameVerifier = AeolusConfig.getHostnameVerifier()
            if (null != hostnameVerifier) {
                it.hostnameVerifier(hostnameVerifier)
            }
        }.build()
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

                    val httpCode = response.code()
                    if (response.isSuccessful) {
                        var bodyString: String? = response.body()?.string()

                        val filter = AeolusConfig.getFilter()
                        if (null != filter) {
                            bodyString = filter.filter(request.url().url().path, bodyString)
                        }

                        sendMessage(Message().apply {
                            what = Success
                            data = Bundle().apply {
                                putInt(RESPONSE_CODE, httpCode)
                                putString(RESPONSE_BODY, bodyString)
                            }
                        })
                    } else {
                        val bodyString: String? = response.body()?.string()
                        val msg = response.message()
                        sendMessage(Message().apply {
                            what = Failure
                            data = Bundle().apply {
                                putInt(RESPONSE_CODE, httpCode)
                                putString(RESPONSE_BODY, bodyString ?: msg)
                            }
                        })
                    }
                } catch (e: SocketTimeoutException) {
                    sendMessage(Message().apply {
                        what = SocketTimeoutException_Code
                        data = Bundle().apply {
                            putString(RESPONSE_BODY, e.localizedMessage)
                        }
                    })
                } catch (e: UnknownHostException) {
                    sendMessage(Message().apply {
                        what = UnknownHostException
                        data = Bundle().apply {
                            putString(RESPONSE_BODY, e.localizedMessage)
                        }
                    })
                } catch (e: ConnectException) {
                    sendMessage(Message().apply {
                        what = ConnectException
                        data = Bundle().apply {
                            putString(RESPONSE_BODY, e.localizedMessage)
                        }
                    })
                } catch (e: IOException) {
                    sendMessage(Message().apply {
                        what = IOException
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
                Success -> {
                    with(msg.data) {
                        val code = getInt(RESPONSE_CODE)
                        val bodyString: String? = getString(RESPONSE_BODY)

                        if (200 == code && !TextUtils.isEmpty(bodyString)) {
                            val types = callback?.javaClass?.genericInterfaces
                            val type = types?.get(0)
                            if (type is ParameterizedType) {
                                val argsTypes = type.actualTypeArguments
                                val argsType = argsTypes.get(0)
                                try {
                                    val obj = JSON.parseObject<T?>(bodyString, argsType)
                                    callback?.onSuccess(obj)
                                } catch (e: Exception) {
                                    callback?.onFailure(AeolusException(code = AEOLUS_CODE_JSON_ERROR, message = e.localizedMessage))
                                }
                            } else {
                                callback?.onFailure(AeolusException(code = AEOLUS_CODE_INTERNAL_ERROR, message = "type is not ParameterizedType"))
                            }
                        }
                    }
                }
                Failure -> {
                    with(msg.data) {
                        val errCode = getInt(RESPONSE_CODE)
                        val errMsg = getString(RESPONSE_BODY)
                        callback?.onFailure(AeolusException(code = BUSINESS_EXCEPTION, businessCode = errCode, message = errMsg))
                    }
                }
                SocketTimeoutException_Code -> {
                    with(msg.data) {
                        val errMsg = getString(RESPONSE_BODY)
                        callback?.onFailure(AeolusException(code = AEOLUS_CODE_SOCKET_ERROR, message = errMsg))
                    }
                }
                ConnectException -> {
                    with(msg.data) {
                        val errMsg = getString(RESPONSE_BODY)
                        callback?.onFailure(AeolusException(code = AEOLUS_CODE_CONNECT_ERROR, message = errMsg))
                    }
                }
                UnknownHostException -> {
                    with(msg.data) {
                        val errMsg = getString(RESPONSE_BODY)
                        callback?.onFailure(AeolusException(code = AEOLUS_CODE_UNKNOWN_HOSTNAME_ERROR, message = errMsg))
                    }
                }
                IOException -> {
                    with(msg.data) {
                        val errMsg = getString(RESPONSE_BODY)
                        callback?.onFailure(AeolusException(code = AEOLUS_CODE_IO_ERROR, message = errMsg))
                    }
                }
            }
            end?.onEnd()
        }
    }
}