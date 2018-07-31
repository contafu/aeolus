package com.olympians.aeolus.annotations

@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
internal annotation class Post(val host: String = "", val api: String = "")