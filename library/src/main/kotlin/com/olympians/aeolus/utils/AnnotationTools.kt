package com.olympians.aeolus.utils

import android.text.TextUtils
import com.alibaba.fastjson.JSON
import com.olympians.aeolus.AeolusRequest
import com.olympians.aeolus.annotations.Get
import com.olympians.aeolus.annotations.Post
import com.olympians.aeolus.annotations.Query
import com.olympians.aeolus.annotations.Strip
import com.olympians.aeolus.config.AeolusConfig
import java.net.URLEncoder

internal object AnnotationTools {

    internal const val MAP_KEY_HOST = "HOST"
    internal const val MAP_KEY_API = "API"
    internal const val MAP_KEY_TYPE = "TYPE"
    internal const val MAP_KEY_METHOD = "METHOD"
    internal const val MAP_KEY_BODY = "BODY"

    internal const val MAP_VALUE_GET = "GET"
    internal const val MAP_VALUE_POST = "POST"

    /**
     * extract parameters from request
     * @param request Request object implemented with AeolusRequest
     * @return A map include parameters
     */
    internal fun extractParams(request: AeolusRequest?): MutableMap<String, Any> {
        if (null == request) {
            throw KotlinNullPointerException("Request can not be null")
        }
        val clazz = request.javaClass

        val getAnno = clazz.getAnnotation(Get::class.java)
        val postAnno = clazz.getAnnotation(Post::class.java)
        if (null == getAnno && null == postAnno) {
            throw Exception("Request must be annotated with Get or Post")
        }
        if (null != getAnno && null != postAnno) {
            throw Exception("Request can not be annotated with Get and Post at the same time")
        }
        val map = mutableMapOf<String, Any>()
        val host = AeolusConfig.getHost()

        if (null != getAnno) {
            val getHost = getAnno.host.trim()
            val getApi = getAnno.api.trim()

            if ("" == getHost) {
                if (null != host && host.isNotEmpty()) {
                    map[MAP_KEY_HOST] = host
                } else {
                    throw IllegalArgumentException("You should add one host")
                }
            } else {
                map[MAP_KEY_HOST] = getHost
            }

            if ("" != getApi) {
                map[MAP_KEY_API] = getApi
            }

            map[MAP_KEY_METHOD] = MAP_VALUE_GET

            recursion(request, clazz, map)
        }

        if (null != postAnno) {
            val postHost = postAnno.host.trim()
            val postApi = postAnno.api.trim()
            val contentType = postAnno.contentType

            if ("" == postHost) {
                if (null != host && host.isNotEmpty()) {
                    map[MAP_KEY_HOST] = host
                } else {
                    throw IllegalArgumentException("You should add one host")
                }
            } else {
                map[MAP_KEY_HOST] = postHost
            }

            if ("" != postApi) {
                map[MAP_KEY_API] = postApi
            }

            if ("" != contentType) {
                map[MAP_KEY_TYPE] = contentType
            } else {
                map[MAP_KEY_TYPE] = ContentType_JSON
            }

            when (contentType) {
                ContentType_JSON -> {
                    val tarField = clazz.declaredFields.find {
                        it.isAccessible = true
                        val stripAnno = it.getAnnotation(Strip::class.java)
                        null !== stripAnno
                    }
                    map[MAP_KEY_BODY] = if (null === tarField) {
                        JSON.toJSONString(request)
                    } else {
                        JSON.toJSONString(tarField.get(request))
                    }
                }
                ContentType_Multipart -> {
                    map[MAP_KEY_BODY] = request
                }
            }

            map[MAP_KEY_METHOD] = MAP_VALUE_POST
        }
        return map
    }

    private fun recursion(classInstance: AeolusRequest, clazz: Class<in AeolusRequest>, map: MutableMap<String, Any>) {
        clazz.declaredFields.filter { field ->
            field.name.let {
                "\$change" != it && "serialVersionUID" != it && "shadow\$_klass" != it && "shadow\$_monitor" != it
            }
        }.forEach {
            it.isAccessible = true
            val k = it.name
            val v = it.get(classInstance)
            if (!TextUtils.isEmpty(k) && null != v) {
                if (v is String) {
                    map[k] = URLEncoder.encode(v, "UTF-8")
                } else {
                    map[k] = v
                }
            }
        }

        val superClass = clazz.superclass
        if (null != superClass?.getAnnotation(Query::class.java)) {
            recursion(classInstance, superClass, map)
        }
    }

}