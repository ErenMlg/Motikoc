package com.softcross.motikoc.common

import java.lang.Exception

sealed class ResponseState<out T : Any> {
    data object Loading : ResponseState<Nothing>()
    data class Success<out T : Any>(val result: T) : ResponseState<T>()
    data class Error(val exception: Exception) : ResponseState<Nothing>()
}