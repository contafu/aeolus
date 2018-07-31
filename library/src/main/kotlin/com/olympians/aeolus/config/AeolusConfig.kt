package com.olympians.aeolus.config

import android.text.TextUtils

object AeolusConfig {

    private var host: String? = null

    private val headerMap: MutableMap<String, String> = mutableMapOf()

    internal fun getHost() = this.host

    internal fun getHeaders(): MutableMap<String, String> = this.headerMap

    fun setHost(host: String): AeolusConfig {
        this.host = host
        return this
    }

    fun addHeader(key: String, value: String): AeolusConfig {
        if (!TextUtils.isEmpty(key) && !TextUtils.isEmpty(value)) {
            this.headerMap[key] = value
        }
        return this
    }

    fun addHeaders(headers: MutableMap<String, String> = mutableMapOf()): AeolusConfig {
        if (!headers.isEmpty()) {
            this.headerMap.putAll(headers)
        }
        return this
    }

}