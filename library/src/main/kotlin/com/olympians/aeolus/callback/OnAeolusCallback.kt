package com.olympians.aeolus.callback

interface OnAeolusCallback<T> {

    fun onSuccess(response: T)

    fun onFailure(code: Int, errMsg: String?)
}