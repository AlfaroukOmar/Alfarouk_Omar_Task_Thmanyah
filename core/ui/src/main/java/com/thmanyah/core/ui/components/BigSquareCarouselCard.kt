package com.thmanyah.core.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.thmanyah.core.designsystem.theme.ThmanyahDimens
import com.thmanyah.core.ui.R
import com.thmanyah.core.ui.util.episodesCountLabel
import com.thmanyah.core.ui.util.formatDurationShort
import com.thmanyah.domain.entities.ContentItem
import androidx.compose.ui.platform.LocalResources
import androidx.compose.ui.tooling.preview.Preview
import com.thmanyah.core.designsystem.theme.ThmanyahTheme
import com.thmanyah.core.fixtures.ContentItemSamples


@Composable
fun BigSquareCarouselCard(
    item: ContentItem,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val res = LocalResources.current
    Column(
        modifier = modifier
            .width(160.dp)
            .clickable(onClick = onClick),
    ) {
        Box {
            ContentImage(
                imageUrl = item.imageUrl,
                contentDescription = item.title,
                size = 160.dp,
                cacheKey = item.id,
                shape = RoundedCornerShape(ThmanyahDimens.cardCorner),
                modifier = Modifier,
            )
            val badge = when (item) {
                is ContentItem.Podcast -> item.popularityScore ?: item.episodeCount
                else -> null
            }
            if (badge != null) {
                Surface(
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .padding(ThmanyahDimens.spaceSm),
                    shape = CircleShape,
                    color = MaterialTheme.colorScheme.surface.copy(alpha = 0.92f),
                ) {
                    Text(
                        text = badge.toString(),
                        style = MaterialTheme.typography.labelMedium,
                        modifier = Modifier.padding(horizontal = ThmanyahDimens.spaceSm, vertical = ThmanyahDimens.spaceXs),
                    )
                }
            }
            val dur = res.formatDurationShort(item.durationSeconds)
            if (dur != null) {
                Surface(
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .padding(ThmanyahDimens.spaceSm),
                    shape = RoundedCornerShape(50),
                    color = MaterialTheme.colorScheme.surface.copy(alpha = 0.92f),
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = ThmanyahDimens.spaceSm, vertical = ThmanyahDimens.spaceXs),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.ic_play_filled_24),
                            contentDescription = null,
                            modifier = Modifier.size(14.dp),
                            tint = MaterialTheme.colorScheme.onSurface,
                        )
                        Text(dur, style = MaterialTheme.typography.labelSmall)
                    }
                }
            }
        }
        Text(
            text = item.title,
            style = MaterialTheme.typography.titleMedium,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.padding(top = ThmanyahDimens.spaceSm),
        )
        val subtitle = when (item) {
            is ContentItem.Podcast -> item.episodeCount?.let { res.episodesCountLabel(it) }
            is ContentItem.AudioBook -> item.authorName
            else -> null
        }
        if (subtitle != null) {
            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
    }
}

@Preview(showBackground = true, name = "Podcast")
@Composable
private fun BigSquareCarouselCardPodcastPreview() {
    ThmanyahTheme {
        BigSquareCarouselCard(
            item = ContentItemSamples.podcast(),
            onClick = {},
            modifier = Modifier.padding(ThmanyahDimens.spaceMd),
        )
    }
}

@Preview(showBackground = true, name = "Episode")
@Composable
private fun BigSquareCarouselCardEpisodePreview() {
    ThmanyahTheme {
        BigSquareCarouselCard(
            item = ContentItemSamples.episode(),
            onClick = {},
            modifier = Modifier.padding(ThmanyahDimens.spaceMd),
        )
    }
}
