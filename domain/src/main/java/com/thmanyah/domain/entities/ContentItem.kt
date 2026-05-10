package com.thmanyah.domain.entities

import kotlinx.datetime.LocalDate

sealed interface ContentItem {
    val id: String
    val title: String
    val imageUrl: String?
    val durationSeconds: Long?

    data class Podcast(
        override val id: String,
        override val title: String,
        override val imageUrl: String?,
        override val durationSeconds: Long?,
        val episodeCount: Int?,
        val language: String?,
        val popularityScore: Int?,
    ) : ContentItem

    data class Episode(
        override val id: String,
        override val title: String,
        override val imageUrl: String?,
        override val durationSeconds: Long?,
        val audioUrl: String?,
        val releaseDate: LocalDate?,
        val podcastName: String?,
        val authorName: String?,
    ) : ContentItem

    data class AudioBook(
        override val id: String,
        override val title: String,
        override val imageUrl: String?,
        override val durationSeconds: Long?,
        val authorName: String?,
        val releaseDate: LocalDate?,
        val language: String?,
    ) : ContentItem

    data class AudioArticle(
        override val id: String,
        override val title: String,
        override val imageUrl: String?,
        override val durationSeconds: Long?,
        val authorName: String?,
        val releaseDate: LocalDate?,
    ) : ContentItem

    /**
     * Fallback when [com.thmanyah.domain.models.ContentType] is unknown or row cannot be parsed safely.
     */
    data class UnknownContent(
        override val id: String,
        override val title: String,
        override val imageUrl: String?,
        override val durationSeconds: Long?,
        val raw: Map<String, String>,
    ) : ContentItem
}