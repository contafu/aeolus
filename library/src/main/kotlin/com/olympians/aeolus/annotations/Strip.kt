package com.olympians.aeolus.annotations

import java.lang.annotation.Inherited

@Inherited
@Target(AnnotationTarget.FIELD)
@Retention(AnnotationRetention.RUNTIME)
annotation class Strip(val s: String = "")