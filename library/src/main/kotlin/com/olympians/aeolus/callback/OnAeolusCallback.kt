package com.olympians.aeolus.callback

import com.olympians.aeolus.exception.AeolusException

interface OnAeolusCallback<T> {

    fun onSuccess(response: T)

    fun onFailure(exception: AeolusException)
}