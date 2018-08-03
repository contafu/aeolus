package com.olympians.aeolus.config

import android.text.TextUtils
import okhttp3.OkHttpClient
import javax.net.ssl.HostnameVerifier

object AeolusConfig {

    private var host: String? = null

    private val headerMap: MutableMap<String, String> = mutableMapOf()

    private var hostnameVerifier: HostnameVerifier? = null

    private var httpClient: OkHttpClient? = null

    private var filter: AeolusFilter? = null

    internal fun getHost() = this.host

    internal fun getHeaders(): MutableMap<String, String> = this.headerMap

    internal fun getHostnameVerifier(): HostnameVerifier? = this.hostnameVerifier

    internal fun getHttpClient(): OkHttpClient? = this.httpClient

    internal fun getFilter(): AeolusFilter? = this.filter

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

    fun setHostnameVerifier(hostnameVerifier: HostnameVerifier): AeolusConfig {
        this.hostnameVerifier = hostnameVerifier
        return this
    }

    fun setHttpClient(httpClient: OkHttpClient): AeolusConfig {
        this.httpClient = httpClient
        return this
    }

    fun addFilter(filter: AeolusFilter): AeolusConfig {
        this.filter = filter
        return this
    }

}