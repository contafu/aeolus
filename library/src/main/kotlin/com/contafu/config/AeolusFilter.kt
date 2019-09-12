package com.contafu.config

interface AeolusFilter {

    fun filter(url: String?, body: String?): String?

}