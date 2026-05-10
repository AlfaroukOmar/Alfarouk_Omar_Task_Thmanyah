package com.thmanyah.network

import com.google.common.truth.Truth.assertThat
import com.thmanyah.core.network.error.ErrorMapper
import com.thmanyah.domain.models.AppError
import kotlinx.serialization.SerializationException
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Test
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

class ErrorMapperTest {

    @Test
    fun mapsUnknownHost() {
        assertThat(ErrorMapper.map(UnknownHostException())).isEqualTo(AppError.NoInternet)
    }

    @Test
    fun mapsTimeout() {
        assertThat(ErrorMapper.map(SocketTimeoutException())).isEqualTo(AppError.Timeout)
    }

    @Test
    fun mapsSerialization() {
        assertThat(ErrorMapper.map(SerializationException("x"))).isEqualTo(AppError.Parsing)
    }

    @Test
    fun mapsHttp503() {
        val body = "".toResponseBody("text/plain".toMediaType())
        val response = Response.error<Unit>(503, body)
        val ex = HttpException(response)
        val err = ErrorMapper.map(ex)
        assertThat(err).isInstanceOf(AppError.Server::class.java)
        assertThat((err as AppError.Server).code).isEqualTo(503)
    }

    @Test
    fun mapsIoToNoInternet() {
        assertThat(ErrorMapper.map(IOException())).isEqualTo(AppError.NoInternet)
    }
}
