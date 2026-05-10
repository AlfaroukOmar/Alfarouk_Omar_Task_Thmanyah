package com.thmanyah.data.remote.mapper

import com.thmanyah.core.commen.extensions.isValidHttpUrl
import com.thmanyah.core.commen.extensions.orEmptyTrimmed
import com.thmanyah.core.commen.extensions.parseReleaseDate
import com.thmanyah.core.commen.extensions.toIntSafe
import com.thmanyah.core.commen.extensions.toLongSafe
import com.thmanyah.data.remote.dto.HomePageDto
import com.thmanyah.data.remote.dto.SectionDto
import com.thmanyah.domain.entities.ContentItem
import com.thmanyah.domain.entities.HomeSection
import com.thmanyah.domain.models.ContentType
import com.thmanyah.domain.models.SectionLayout
import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.booleanOrNull
import kotlinx.serialization.json.intOrNull
import kotlinx.serialization.json.longOrNull
import javax.inject.Inject
import kotlin.collections.component1
import kotlin.collections.component2

class SectionMapper @Inject constructor() {

    @OptIn(InternalSerializationApi::class)
    fun mapPage(dto: HomePageDto, dedupeContentIds: Boolean = true): List<HomeSection> =
        dto.sections.mapNotNull { mapSection(it, dedupeContentIds) }

    @OptIn(InternalSerializationApi::class)
    fun mapSection(dto: SectionDto, dedupeContentIds: Boolean = true): HomeSection? = runCatching {
        val title = dto.name.orEmptyTrimmed()
        if (title.isEmpty()) return@runCatching null
        val layout = normalizeLayout(dto.type)
        val contentType = normalizeContentType(dto.content_type)
        val order = dto.order.toOrderInt()
        val mapped = dto.content?.mapNotNull { element ->
            val obj = element as? JsonObject ?: return@mapNotNull null
            runCatching { mapContentItem(obj, contentType) }.getOrNull()
        } ?: emptyList()
        val items = if (dedupeContentIds) mapped.distinctBy { it.id } else mapped
        val id = sectionId(layout, contentType, order, title)
        HomeSection(
            id = id,
            title = title,
            layout = layout,
            contentType = contentType,
            order = order,
            items = items,
        )
    }.getOrNull()

    fun sectionId(layout: SectionLayout, contentType: ContentType, order: Int, title: String): String =
        "${layout.name}-${contentType.name}-$order-${title.hashCode()}"

    internal fun normalizeLayout(raw: String?): SectionLayout {
        val n = raw?.trim()?.lowercase()?.replace(" ", "_")?.replace("-", "_") ?: return SectionLayout.Unknown
        return when (n) {
            "square" -> SectionLayout.Square
            "2_lines_grid" -> SectionLayout.TwoLinesGrid
            "big_square" -> SectionLayout.BigSquare
            "queue" -> SectionLayout.Queue
            "lounge" -> SectionLayout.Lounge
            else -> SectionLayout.Unknown
        }
    }

    internal fun normalizeContentType(raw: String?): ContentType {
        val n = raw?.trim()?.lowercase()?.replace(" ", "_") ?: return ContentType.Unknown
        return when (n) {
            "podcast" -> ContentType.Podcast
            "episode" -> ContentType.Episode
            "audio_book", "audiobook" -> ContentType.AudioBook
            "audio_article" -> ContentType.AudioArticle
            else -> ContentType.Unknown
        }
    }

    private fun mapContentItem(obj: JsonObject, type: ContentType): ContentItem? = when (type) {
        ContentType.Podcast -> mapPodcast(obj)
        ContentType.Episode -> mapEpisode(obj)
        ContentType.AudioBook -> mapAudiobook(obj)
        ContentType.AudioArticle -> mapAudioArticle(obj)
        ContentType.Unknown -> mapUnknown(obj)
    }

    private fun mapPodcast(obj: JsonObject): ContentItem.Podcast? {
        val id = obj.string("podcast_id") ?: return null
        val title = obj.string("name").orEmptyTrimmed().ifEmpty { return null }
        val image = obj.string("avatar_url")?.takeIf { it.isValidHttpUrl() }
        val duration = obj["duration"]?.toDurationFromJson()
        val episodeCount = obj.string("episode_count")?.toIntSafe()
        val language = obj.string("language")
        val popularity = obj.string("popularityScore")?.toIntSafe()
            ?: obj.string("popularityscore")?.toIntSafe()
        return ContentItem.Podcast(
            id = id,
            title = title,
            imageUrl = image,
            durationSeconds = duration,
            episodeCount = episodeCount,
            language = language,
            popularityScore = popularity,
        )
    }

    private fun mapEpisode(obj: JsonObject): ContentItem.Episode? {
        val id = obj.string("episode_id") ?: return null
        val title = obj.string("name").orEmptyTrimmed().ifEmpty { return null }
        val image = obj.string("avatar_url")?.takeIf { it.isValidHttpUrl() }
        val duration = obj["duration"]?.toDurationFromJson()
        val audio = obj.string("audio_url")?.takeIf { it.isNotBlank() }
            ?: obj.string("separated_audio_url")?.takeIf { it.isNotBlank() }
        val release = obj.string("release_date")?.parseReleaseDate()
        val podcastName = obj.string("podcast_name")
        val author = obj.string("author_name")
        return ContentItem.Episode(
            id = id,
            title = title,
            imageUrl = image,
            durationSeconds = duration,
            audioUrl = audio,
            releaseDate = release,
            podcastName = podcastName,
            authorName = author,
        )
    }

    private fun mapAudiobook(obj: JsonObject): ContentItem.AudioBook? {
        val id = obj.string("audiobook_id") ?: return null
        val title = obj.string("name").orEmptyTrimmed().ifEmpty { return null }
        val image = obj.string("avatar_url")?.takeIf { it.isValidHttpUrl() }
        val duration = obj["duration"]?.toDurationFromJson()
        val author = obj.string("author_name")
        val release = obj.string("release_date")?.parseReleaseDate()
        val lang = obj.string("language")
        return ContentItem.AudioBook(
            id = id,
            title = title,
            imageUrl = image,
            durationSeconds = duration,
            authorName = author,
            releaseDate = release,
            language = lang,
        )
    }

    private fun mapAudioArticle(obj: JsonObject): ContentItem.AudioArticle? {
        val id = obj.string("article_id") ?: return null
        val title = obj.string("name").orEmptyTrimmed().ifEmpty { return null }
        val image = obj.string("avatar_url")?.takeIf { it.isValidHttpUrl() }
        val duration = obj["duration"]?.toDurationFromJson()
        val author = obj.string("author_name")
        val release = obj.string("release_date")?.parseReleaseDate()
        return ContentItem.AudioArticle(
            id = id,
            title = title,
            imageUrl = image,
            durationSeconds = duration,
            authorName = author,
            releaseDate = release,
        )
    }

    private fun mapUnknown(obj: JsonObject): ContentItem {
        val rawStrings = obj.entries.associate { (k, v) ->
            k to extractPrimitiveString(v)
        }
        val id = rawStrings["podcast_id"]
            ?: rawStrings["episode_id"]
            ?: rawStrings["audiobook_id"]
            ?: rawStrings["article_id"]
            ?: "unknown-${rawStrings.values.joinToString().hashCode()}"
        val title = rawStrings["name"].orEmptyTrimmed().ifEmpty { "—" }
        val image = rawStrings["avatar_url"]?.takeIf { it.isValidHttpUrl() }
        val duration = obj["duration"]?.toDurationFromJson()
        return ContentItem.UnknownContent(
            id = id,
            title = title,
            imageUrl = image,
            durationSeconds = duration,
            raw = rawStrings,
        )
    }

    private fun extractPrimitiveString(v: JsonElement): String =
        (v as? JsonPrimitive)?.let { pr ->
            when {
                pr.isString -> pr.content
                pr.longOrNull != null -> pr.longOrNull.toString()
                pr.intOrNull != null -> pr.intOrNull.toString()
                pr.booleanOrNull != null -> pr.booleanOrNull.toString()
                else -> v.toString()
            }
        } ?: ""

    private fun JsonObject.string(key: String): String? =
        this[key]?.let { el ->
            (el as? JsonPrimitive)?.let { pr ->
                when {
                    pr.isString -> pr.content
                    pr.longOrNull != null -> pr.longOrNull.toString()
                    pr.intOrNull != null -> pr.intOrNull.toString()
                    else -> null
                }
            }
        }

    private fun JsonElement.toDurationFromJson(): Long? {
        val pr = this as? JsonPrimitive ?: return null
        return when {
            pr.isString -> pr.content.toLongSafe()?.takeIf { it in 0..172_800L }
            pr.longOrNull != null -> pr.longOrNull!!.takeIf { it in 0..172_800L }
            pr.intOrNull != null -> pr.intOrNull!!.toLong().takeIf { it in 0..172_800L }
            else -> null
        }
    }
}

internal fun JsonElement?.toOrderInt(): Int {
    val el = this ?: return Int.MAX_VALUE
    val pr = el as? JsonPrimitive ?: return Int.MAX_VALUE
    return when {
        pr.isString -> pr.content.toIntSafe() ?: Int.MAX_VALUE
        pr.longOrNull != null -> pr.longOrNull!!.toInt()
        pr.intOrNull != null -> pr.intOrNull!!
        else -> Int.MAX_VALUE
    }
}