package com.contafu.annotations

import java.lang.annotation.Inherited

@Inherited
@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
annotation class Post(val host: String = "", val api: String = "", val contentType: String = "")