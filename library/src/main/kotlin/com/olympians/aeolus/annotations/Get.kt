package com.olympians.aeolus.annotations

@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
annotation class Get(val host: String = "", val api: String = "")