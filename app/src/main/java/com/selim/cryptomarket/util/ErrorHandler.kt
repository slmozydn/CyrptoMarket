package com.selim.cryptomarket.util

import kotlinx.serialization.SerializationException
import retrofit2.HttpException
import timber.log.Timber
import java.io.IOException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ErrorHandler @Inject constructor() {

    fun handleError(exception: Throwable): String {
        Timber.e(exception)

        return when (exception) {
            is HttpException -> handleHttpError(exception)
            is SerializationException -> "Data processing error occurred"
            is UnknownHostException -> "No internet connection"
            is SocketTimeoutException -> "Connection timeout. Please try again"
            is IOException -> "Network error occurred. Please try again"
            else -> "An unknown error occurred"
        }
    }

    fun handleHttpError(exception: HttpException): String = when (exception.code()) {
        401 -> "Unauthorized access. Please try again"
        403 -> "Access forbidden"
        404 -> "Requested resource not found"
        else -> "An unknown error occurred"
    }
}
