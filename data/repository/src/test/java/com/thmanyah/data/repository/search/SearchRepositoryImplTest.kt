package com.thmanyah.data.repository.search

import com.google.common.truth.Truth.assertThat
import com.thmanyah.data.remote.api.SearchApi
import com.thmanyah.data.remote.dto.SearchResponseDto
import com.thmanyah.data.remote.dto.SectionDto
import com.thmanyah.data.remote.mapper.SectionMapper
import com.thmanyah.domain.models.AppError
import com.thmanyah.domain.models.AppResult
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.buildJsonObject
import org.junit.Test
import java.io.IOException
import kotlin.jvm.java

class SearchRepositoryImplTest {

    private val mapper = SectionMapper()


    @OptIn(InternalSerializationApi::class)
    @Test
    fun success_mapsSections() = runTest {
        val api = mockk<SearchApi>()
        val dto = SectionDto(
            name = "S",
            type = "square",
            content_type = "podcast",
            order = JsonPrimitive(0),
            content = JsonArray(
                listOf(
                    buildJsonObject {
                        put("podcast_id", JsonPrimitive("p1"))
                        put("name", JsonPrimitive("Show"))
                    },
                ),
            ),
        )
        coEvery { api.search("q") } returns SearchResponseDto(sections = listOf(dto))
        val repo = SearchRepositoryImpl(api, mapper, Dispatchers.Unconfined)
        val result = repo.search("q").first() as AppResult.Success
        assertThat(result.data).hasSize(1)
        assertThat(result.data.single().items).hasSize(1)
    }

    @OptIn(InternalSerializationApi::class)
    @Test
    fun networkFailure_mapsNoInternet() = runTest {
        val api = mockk<SearchApi>()
        coEvery { api.search(any()) } throws IOException("boom")
        val repo = SearchRepositoryImpl(api, mapper, Dispatchers.Unconfined)
        val result = repo.search("q").first() as AppResult.Failure
        assertThat(result.error).isInstanceOf(AppError.NoInternet::class.java)
    }
}