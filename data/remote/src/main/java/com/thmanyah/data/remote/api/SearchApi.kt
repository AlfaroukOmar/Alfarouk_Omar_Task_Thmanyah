package com.thmanyah.data.remote.api

import com.thmanyah.data.remote.dto.SearchResponseDto
import kotlinx.serialization.InternalSerializationApi
import retrofit2.http.GET
import retrofit2.http.Query

interface SearchApi {
    @OptIn(InternalSerializationApi::class)
    @GET("search")
    suspend fun search(@Query("q") query: String): SearchResponseDto
}