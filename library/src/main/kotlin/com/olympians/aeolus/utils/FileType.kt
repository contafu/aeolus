package com.olympians.aeolus.utils

import android.text.TextUtils
import java.util.*

internal fun getContentType(extensions: String): String {
    return if (!TextUtils.isEmpty(extensions)) {
        contentTypeMap[extensions.toLowerCase(Locale.CHINA)] ?: "application/octet-stream"
    } else {
        "application/octet-stream"
    }
}

private val contentTypeMap = hashMapOf(
        Pair("png", "image/png"),
        Pair("jpe", "image/jpeg"),
        Pair("jpg", "image/jpeg"),
        Pair("jpeg", "image/jpeg"),
        Pair("gif", "image/gif"),
        Pair("wbmp", "image/vnd.wap.wbmp"),
        Pair("ico", "image/x-icon"),
        Pair("tif", "image/tiff"),
        Pair("tiff", "image/tiff"),
        Pair("fax", "image/fax"),
        Pair("jfif", "image/x-icon"),
        Pair("net", "image/pnetvue"),
        Pair("rp", "image/vnd.rn-realpix"),
        Pair("asf", "video/x-ms-asf"),
        Pair("asx", "video/x-ms-asf"),
        Pair("avi", "video/avi"),
        Pair("ivf", "video/x-ivf"),
        Pair("m1v", "video/x-mpeg"),
        Pair("m2v", "video/x-mpeg"),
        Pair("mpe", "video/x-mpeg"),
        Pair("mps", "video/x-mpeg"),
        Pair("m4e", "video/mpeg4"),
        Pair("mp4", "video/mpeg4"),
        Pair("mp2v", "video/mpeg"),
        Pair("mpv2", "video/mpeg"),
        Pair("rv", "video/vnd.rn-realvideo"),
        Pair("wm", "video/x-ms-wm"),
        Pair("wmv", "video/x-ms-wmv"),
        Pair("wmx", "video/x-ms-wmx"),
        Pair("wvx", "video/x-ms-wvx"),
        Pair("mpeg", "video/mpg"),
        Pair("mpg", "video/mpg"),
        Pair("mpv", "video/mpg"),
        Pair("mpa", "video/x-mpg"),
        Pair("movie", "video/x-sgi-movie")
)