package com.thmanyah.core.ui.model

import com.thmanyah.domain.entities.ContentItem
import kotlinx.datetime.LocalDate

fun ContentItem.releaseDateOrNull(): LocalDate? = when (this) {
    is ContentItem.Episode -> releaseDate
    is ContentItem.AudioBook -> releaseDate
    is ContentItem.AudioArticle -> releaseDate
    else -> null
}

fun ContentItem.displaySubtitleForLounge(): String = when (this) {
    is ContentItem.Episode -> podcastName ?: authorName ?: title
    is ContentItem.AudioArticle -> authorName ?: title
    is ContentItem.AudioBook -> authorName ?: title
    is ContentItem.Podcast -> title
    is ContentItem.UnknownContent -> title
}
