package com.olympians.aeolus.utils

import android.text.TextUtils
import com.alibaba.fastjson.JSON
import com.olympians.aeolus.AeolusRequest
import com.olympians.aeolus.annotations.Get
import com.olympians.aeolus.annotations.Post
import com.olympians.aeolus.annotations.Query
import com.olympians.aeolus.config.AeolusConfig

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
                if (null != host && !host.isEmpty()) {
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
                if (null != host && !host.isEmpty()) {
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
            }

            val jsonString = JSON.toJSONString(request)

            map[MAP_KEY_BODY] = jsonString

            map[MAP_KEY_METHOD] = MAP_VALUE_POST
        }
        return map
    }

    private fun recursion(classInstance: AeolusRequest, clazz: Class<in AeolusRequest>, map: MutableMap<String, Any>) {
        clazz.declaredFields.filter {
            it.name.let {
                null != it && "\$change" != it
                        && "serialVersionUID" != it
                        && "shadow\$_klass" != it
                        && "shadow\$_monitor" != it
            }
        }.forEach {
            it.isAccessible = true
            val k = it.name
            val v = it.get(classInstance)
            if (!TextUtils.isEmpty(k) && null != v) {
                map[k] = v
            }
        }

        if ((null != clazz.superclass.getAnnotation(Query::class.java))) {
            recursion(classInstance, clazz.superclass, map)
        }
    }

}