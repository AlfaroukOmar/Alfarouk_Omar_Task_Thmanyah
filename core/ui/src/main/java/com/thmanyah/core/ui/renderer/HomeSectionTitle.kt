package com.thmanyah.core.ui.renderer

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.thmanyah.core.ui.R
import com.thmanyah.domain.entities.HomeSection
import com.thmanyah.domain.models.ContentType


@Composable
fun HomeSection.displayTitle(): String = when (contentType) {
    ContentType.Podcast -> stringResource(R.string.section_title_podcast)
    ContentType.Episode -> stringResource(R.string.section_title_episode)
    ContentType.AudioBook -> stringResource(R.string.section_title_audiobook)
    ContentType.AudioArticle -> stringResource(R.string.section_title_audio_article)
    ContentType.Unknown -> title
}
