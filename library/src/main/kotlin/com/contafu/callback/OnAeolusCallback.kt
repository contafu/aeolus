package com.contafu.callback

import com.contafu.exception.AeolusException

interface OnAeolusCallback<T> {

    fun onSuccess(response: T)

    fun onFailure(exception: AeolusException)
}