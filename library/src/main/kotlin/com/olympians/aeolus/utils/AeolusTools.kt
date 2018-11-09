package com.olympians.aeolus.utils

import android.text.TextUtils
import com.olympians.aeolus.config.AeolusConfig
import com.olympians.aeolus.utils.AnnotationTools.MAP_KEY_API
import com.olympians.aeolus.utils.AnnotationTools.MAP_KEY_BODY
import com.olympians.aeolus.utils.AnnotationTools.MAP_KEY_HOST
import com.olympians.aeolus.utils.AnnotationTools.MAP_KEY_METHOD
import com.olympians.aeolus.utils.AnnotationTools.MAP_KEY_TYPE
import com.olympians.aeolus.utils.AnnotationTools.MAP_VALUE_GET
import com.olympians.aeolus.utils.AnnotationTools.MAP_VALUE_POST
import okhttp3.MediaType
import okhttp3.Request
import okhttp3.RequestBody

internal object AeolusTools {

    internal fun buildRequest(map: MutableMap<String, Any>): Request {
        val request = Request.Builder().apply {
            AeolusConfig.getHeaders().forEach { (k, v) ->
                header(k, v)
            }
        }

        val method = map[MAP_KEY_METHOD]
        return when (method) {
            MAP_VALUE_GET -> {
                request.url(buildGetUrl(map)).build()
            }
            MAP_VALUE_POST -> {
                request.url(buildPostUrl(map)).post(buildPostBody(map)).build()
            }
            else -> {
                request.build()
            }
        }
    }

    private fun buildGetUrl(map: MutableMap<String, Any>): String {
        var host = map[MAP_KEY_HOST]
        var api = map[MAP_KEY_API]

        if (host is String) {
            host = filterBiasLineEnd(host)
        }
        if (api is String) {
            api = filterBiasLineStart(api)
            api = filterBiasLineEnd(api)
        }

        val sb = StringBuilder().apply {
            append(host)
            if (null != api) {
                append(api)
            }
            append("?")
        }

        val paramsArr = mutableListOf<String>()
        map.filter { entry ->
            entry.key.let { MAP_KEY_HOST != it && MAP_KEY_API != it && MAP_KEY_METHOD != it }
        }.forEach {
            paramsArr.add("${it.key}=${it.value}")
        }

        return sb.append(paramsArr.joinToString("&")).toString()
    }

    private fun buildPostUrl(map: MutableMap<String, Any>): String {
        var host = map[MAP_KEY_HOST]
        var api = map[MAP_KEY_API]

        if (host is String) {
            host = filterBiasLineEnd(host)
        }
        if (api is String) {
            api = filterBiasLineStart(api)
            api = filterBiasLineEnd(api)
        }

        val sb = StringBuilder().apply {
            append(host)
            if (null != api) {
                append(api)
            }
        }
        return sb.toString()
    }

    private fun buildPostBody(map: MutableMap<String, Any>): RequestBody {
        val contentType = map[MAP_KEY_TYPE]
        val body = map[MAP_KEY_BODY] as String
        return if (contentType is String && "" != contentType) {
            RequestBody.create(MediaType.parse(contentType), body)
        } else {
            RequestBody.create(MediaType.parse("application/json;charset=UTF-8"), body)
        }
    }

    private fun filterBiasLineStart(res: String): String {
        return if (!TextUtils.isEmpty(res) && !res.startsWith("/", true)) "/$res" else res
    }

    private fun filterBiasLineEnd(res: String): String {
        return if (!TextUtils.isEmpty(res) && res.endsWith("/", true)) res.trimEnd('/') else res
    }
}