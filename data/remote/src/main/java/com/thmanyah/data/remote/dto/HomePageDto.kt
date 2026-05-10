
package com.thmanyah.data.remote.dto

import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonElement


@InternalSerializationApi
@Serializable
data class HomePageDto(
    val sections: List<SectionDto> = emptyList(),
    val pagination: PaginationDto? = null,
)

@InternalSerializationApi
@Serializable
data class PaginationDto(
    val next_page: String? = null,
    val total_pages: Int? = null,
)

@InternalSerializationApi
@Serializable
data class SectionDto(
    val name: String? = null,
    val type: String? = null,
    val content_type: String? = null,
    val order: JsonElement? = null,
    val content: JsonArray? = null,
)

@InternalSerializationApi
@Serializable
data class SearchResponseDto(
    val sections: List<SectionDto> = emptyList(),
)
