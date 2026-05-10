package com.thmanyah.domain.models

sealed class AppError : Throwable() {
    data object NoInternet : AppError()
    data object Timeout : AppError()
    data class Server(val code: Int) : AppError()
    data object Parsing : AppError()
    data class Unknown(val throwable: Throwable?) : AppError()
}
