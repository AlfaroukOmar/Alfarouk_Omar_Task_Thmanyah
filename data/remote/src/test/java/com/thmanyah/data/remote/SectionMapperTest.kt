package com.thmanyah.data.remote

import com.google.common.truth.Truth.assertThat
import com.thmanyah.data.remote.dto.SectionDto
import com.thmanyah.data.remote.mapper.SectionMapper
import com.thmanyah.data.remote.mapper.toOrderInt
import com.thmanyah.domain.models.ContentType
import com.thmanyah.domain.models.SectionLayout
import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.buildJsonObject
import org.junit.Test

class SectionMapperTest {

    private val mapper = SectionMapper()

    @Test
    fun normalizesBigSquareVariants() {
        assertThat(mapper.normalizeLayout("big_square")).isEqualTo(SectionLayout.BigSquare)
        assertThat(mapper.normalizeLayout("big square")).isEqualTo(SectionLayout.BigSquare)
    }

    @Test
    fun unknownLayout() {
        assertThat(mapper.normalizeLayout("weird")).isEqualTo(SectionLayout.Unknown)
    }

    @Test
    fun loremContentTypeUnknown() {
        assertThat(mapper.normalizeContentType("lorem ipsum")).isEqualTo(ContentType.Unknown)
    }

    @Test
    fun orderFromStringDigits() {
        assertThat(JsonPrimitive("3").toOrderInt()).isEqualTo(3)
    }

    @Test
    fun orderInvalidUsesMax() {
        assertThat(JsonPrimitive("not-a-number").toOrderInt()).isEqualTo(Int.MAX_VALUE)
    }

    @OptIn(InternalSerializationApi::class)
    @Test
    fun mapSection_skipsInvalidContentElements() {
        val section = SectionDto(
            name = "Sec",
            type = "square",
            content_type = "episode",
            order = JsonPrimitive(1),
            content = JsonArray(
                listOf(
                    JsonPrimitive("not-an-object"),
                    buildJsonObject {
                        put("episode_id", JsonPrimitive("e1"))
                        put("name", JsonPrimitive("Ok"))
                    },
                ),
            ),
        )
        val out = mapper.mapSection(section)!!
        assertThat(out.items).hasSize(1)
        assertThat(out.items.single().id).isEqualTo("e1")
    }
}