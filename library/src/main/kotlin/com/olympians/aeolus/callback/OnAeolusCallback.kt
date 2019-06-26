package com.olympians.aeolus.callback

import androidx.annotation.NonNull
import androidx.annotation.Nullable
import com.olympians.aeolus.exception.AeolusException

interface OnAeolusCallback<T> {

    fun onSuccess(@Nullable response: T?)

    fun onFailure(@NonNull exception: AeolusException)
}