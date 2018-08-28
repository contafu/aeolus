package com.olympians.aeolus.config

interface AeolusFilter {

    fun filter(url: String?, body: String?): String?

}