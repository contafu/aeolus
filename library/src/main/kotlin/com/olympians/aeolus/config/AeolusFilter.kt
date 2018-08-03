package com.olympians.aeolus.config

interface AeolusFilter {

    fun filter(body: String?): String?

}