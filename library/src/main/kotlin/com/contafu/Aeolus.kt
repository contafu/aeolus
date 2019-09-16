package com.contafu

import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.text.TextUtils
import com.alibaba.fastjson.JSON
import com.contafu.callback.OnAeolusCallback
import com.contafu.callback.OnAeolusEnd
import com.contafu.callback.OnAeolusStart
import com.contafu.config.AeolusConfig
import com.contafu.config.AeolusConfig.TIMEOUT_CONFIG_TIME
import com.contafu.config.AeolusConfig.TIMEOUT_CONFIG_UNIT
import com.contafu.exception.AeolusException
import com.contafu.utils.AeolusTools
import com.contafu.utils.AnnotationTools
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Response
import java.io.IOException
import java.lang.reflect.ParameterizedType
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import java.util.concurrent.Executors
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
        }.build()
    }

    private val threadPool by lazy { Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors() + 1) }

    class Builder<T> : Handler() {
        private var request: AeolusRequest? = null
        private var callback: OnAeolusCallback<T>? = null
        private var lCallback: ((AeolusException?, T?) -> Unit)? = null
        private var start: OnAeolusStart? = null
        private var lStart: (() -> Unit)? = null
        private var end: OnAeolusEnd? = null
        private var lEnd: (() -> Unit)? = null

        fun addRequest(request: AeolusRequest): Builder<T> {
            this.request = request
            return this
        }

        fun addCallback(callback: OnAeolusCallback<T>): Builder<T> {
            this.lCallback = null
            this.callback = callback
            return this
        }

        /* lambda support */
        fun addCallback(callback: (AeolusException?, T?) -> Unit): Builder<T> {
            this.callback = null
            this.lCallback = callback
            return this
        }

        fun addOnStart(start: OnAeolusStart): Builder<T> {
            this.lStart = null
            this.start = start
            return this
        }

        /* lambda support */
        fun addOnStart(start: () -> Unit): Builder<T> {
            this.start = null
            this.lStart = start
            return this
        }

        fun addOnEnd(end: OnAeolusEnd): Builder<T> {
            this.lEnd = null
            this.end = end
            return this
        }

        /* lambda support */
        fun addOnEnd(end: () -> Unit): Builder<T> {
            this.end = null
            this.lEnd = end
            return this
        }

        fun launch() {
            start?.onStart()
            lStart?.invoke()
            GlobalScope.launch(Dispatchers.IO) {
                val request = AeolusTools.buildRequest(AnnotationTools.extractParams(request))
                val response = try {
                    client.newCall(request).execute()
                } catch (e: Exception) {
                    null
                }
                withContext(Dispatchers.Main) {
                    if (null != response) {
                        val responseCode = response.code
                    } else {

                    }
                }
            }
        }

        @Deprecated("use launch() instead")
        fun build() {
            start?.onStart()
            lStart?.invoke()

            threadPool.execute {
                val request = AeolusTools.buildRequest(AnnotationTools.extractParams(request))

                var response: Response? = null
                try {
                    response = client.newCall(request).execute()

                    val httpCode = response.code
                    if (response.isSuccessful) {
                        var bodyString: String? = response.body?.string()

                        val filter = AeolusConfig.getFilter()
                        if (null != filter) {
                            bodyString = filter.filter(request.url.toUrl().path, bodyString)
                        }

                        sendMessage(Message().apply {
                            what = Success
                            data = Bundle().apply {
                                putInt(RESPONSE_CODE, httpCode)
                                putString(RESPONSE_BODY, bodyString)
                            }
                        })
                    } else {
                        val bodyString: String? = response.body?.string()
                        val msg = response.message
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
            }
        }

        override fun handleMessage(msg: Message) {
            super.handleMessage(msg)
            when (msg.what) {
                Success -> {
                    with(msg.data) {
                        val code = getInt(RESPONSE_CODE)
                        val bodyString: String? = getString(RESPONSE_BODY)

                        if (200 == code && !TextUtils.isEmpty(bodyString)) {
                            val types = if (null != callback) callback?.javaClass?.genericInterfaces else lCallback?.javaClass?.genericInterfaces
                            val type = types?.get(0)
                            if (type is ParameterizedType) {
                                val argsTypes = type.actualTypeArguments
                                val argsType = if (null != callback) argsTypes[0] else argsTypes[1]
                                try {
                                    val obj = JSON.parseObject<T>(bodyString, argsType)
                                    callback?.onSuccess(obj)
                                    lCallback?.invoke(null, obj)
                                } catch (e: Exception) {
                                    callback?.onFailure(AeolusException(code = AEOLUS_CODE_JSON_ERROR, message = e.localizedMessage))
                                    lCallback?.invoke(AeolusException(code = AEOLUS_CODE_JSON_ERROR, message = e.localizedMessage), null)
                                }
                            } else {
                                callback?.onFailure(AeolusException(code = AEOLUS_CODE_INTERNAL_ERROR, message = "type is not ParameterizedType"))
                                lCallback?.invoke(AeolusException(code = AEOLUS_CODE_INTERNAL_ERROR, message = "type is not ParameterizedType"), null)
                            }
                        }
                    }
                }
                Failure -> {
                    with(msg.data) {
                        val errCode = getInt(RESPONSE_CODE)
                        val errMsg = getString(RESPONSE_BODY)
                        callback?.onFailure(AeolusException(code = BUSINESS_EXCEPTION, businessCode = errCode, message = errMsg))
                        lCallback?.invoke(AeolusException(code = BUSINESS_EXCEPTION, businessCode = errCode, message = errMsg), null)
                    }
                }
                SocketTimeoutException_Code -> {
                    with(msg.data) {
                        val errMsg = getString(RESPONSE_BODY)
                        callback?.onFailure(AeolusException(code = AEOLUS_CODE_SOCKET_ERROR, message = errMsg))
                        lCallback?.invoke(AeolusException(code = AEOLUS_CODE_SOCKET_ERROR, message = errMsg), null)
                    }
                }
                ConnectException -> {
                    with(msg.data) {
                        val errMsg = getString(RESPONSE_BODY)
                        callback?.onFailure(AeolusException(code = AEOLUS_CODE_CONNECT_ERROR, message = errMsg))
                        lCallback?.invoke(AeolusException(code = AEOLUS_CODE_CONNECT_ERROR, message = errMsg), null)
                    }
                }
                UnknownHostException -> {
                    with(msg.data) {
                        val errMsg = getString(RESPONSE_BODY)
                        callback?.onFailure(AeolusException(code = AEOLUS_CODE_UNKNOWN_HOSTNAME_ERROR, message = errMsg))
                        lCallback?.invoke(AeolusException(code = AEOLUS_CODE_UNKNOWN_HOSTNAME_ERROR, message = errMsg), null)
                    }
                }
                IOException -> {
                    with(msg.data) {
                        val errMsg = getString(RESPONSE_BODY)
                        callback?.onFailure(AeolusException(code = AEOLUS_CODE_IO_ERROR, message = errMsg))
                        lCallback?.invoke(AeolusException(code = AEOLUS_CODE_IO_ERROR, message = errMsg), null)
                    }
                }
            }
            end?.onEnd()
            lEnd?.invoke()
        }
    }
}