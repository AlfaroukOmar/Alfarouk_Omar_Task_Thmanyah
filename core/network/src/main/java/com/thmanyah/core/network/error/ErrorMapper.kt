package com.thmanyah.core.network.error

import com.thmanyah.domain.models.AppError
import kotlinx.serialization.SerializationException
import retrofit2.HttpException
import java.io.IOException
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

object ErrorMapper {
    fun map(throwable: Throwable): AppError = when (throwable) {
        is SerializationException -> AppError.Parsing
        is UnknownHostException, is ConnectException -> AppError.NoInternet
        is SocketTimeoutException -> AppError.Timeout
        is HttpException -> {
            val code = throwable.code()
            if (code in 500..599) AppError.Server(code) else AppError.Unknown(throwable)
        }
        is IOException -> AppError.NoInternet
        else -> AppError.Unknown(throwable)
    }
}
