package com.thmanyah.data.remote.api

import com.thmanyah.data.remote.dto.HomePageDto
import kotlinx.serialization.InternalSerializationApi
import retrofit2.http.GET
import retrofit2.http.Query

interface HomeApi {
    @OptIn(InternalSerializationApi::class)
    @GET("home_sections")
    suspend fun getHome(@Query("page") page: Int = 1): HomePageDto
}
