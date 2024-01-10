package com.example.cryptotrack.handler

sealed class ResultHandler<T>(val data: T? = null, val message: String? = null) {
    class Success<T>(data: T) : ResultHandler<T>(data)
    class Error<T>(message: String, data: T?=null) : ResultHandler<T>(data, message)
    class Loading<T> : ResultHandler<T>()
}
