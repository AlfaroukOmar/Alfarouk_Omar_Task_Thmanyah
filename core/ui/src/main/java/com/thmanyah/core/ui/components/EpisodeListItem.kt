package com.thmanyah.core.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.MoreHoriz
import androidx.compose.material.icons.outlined.PlaylistAdd
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.tooling.preview.Preview
import com.thmanyah.core.designsystem.theme.ThmanyahTheme
import com.thmanyah.core.fixtures.ContentItemSamples
import com.thmanyah.domain.entities.ContentItem
import androidx.compose.ui.platform.LocalResources
import com.thmanyah.core.designsystem.theme.ThmanyahDimens
import com.thmanyah.core.ui.R
import com.thmanyah.core.ui.model.releaseDateOrNull
import com.thmanyah.core.ui.util.formatDurationShort
import com.thmanyah.core.ui.util.formatRelativePublishDate


@Composable
fun EpisodeListItem(
    item: ContentItem,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    onAddToQueue: () -> Unit = {},
    onMore: () -> Unit = {},
) {
    val res = LocalResources.current
    CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Ltr) {
        Row(
            modifier = modifier
                .fillMaxWidth()
                .height(100.dp)
                .clickable(onClick = onClick)
                .padding(horizontal = ThmanyahDimens.spaceMd, vertical = ThmanyahDimens.spaceSm),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(ThmanyahDimens.spaceSm),
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceEvenly,
            ) {
                IconButton(onClick = onAddToQueue, modifier = Modifier.size(ThmanyahDimens.touchTargetMin)) {
                    Icon(
                        Icons.Outlined.PlaylistAdd,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onSurface,
                    )
                }
                IconButton(onClick = onMore, modifier = Modifier.size(ThmanyahDimens.touchTargetMin)) {
                    Icon(
                        Icons.Outlined.MoreHoriz,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onSurface,
                    )
                }
            }
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(ThmanyahDimens.spaceXs),
            ) {
                val rel = res.formatRelativePublishDate(item.releaseDateOrNull())
                if (rel != null) {
                    Text(
                        text = rel,
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
                Text(
                    text = item.title,
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.onSurface,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
                val dur = res.formatDurationShort(item.durationSeconds)
                if (dur != null) {
                    Surface(
                        shape = RoundedCornerShape(50),
                        color = MaterialTheme.colorScheme.surfaceContainerHigh,
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = ThmanyahDimens.spaceSm, vertical = ThmanyahDimens.spaceXs),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(ThmanyahDimens.spaceXs),
                        ) {
                            Icon(
                                painter = painterResource(R.drawable.ic_play_filled_24),
                                contentDescription = null,
                                modifier = Modifier.size(14.dp),
                                tint = MaterialTheme.colorScheme.onSurface,
                            )
                            Text(
                                text = dur,
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onSurface,
                            )
                        }
                    }
                }
            }
            ContentImage(
                imageUrl = item.imageUrl,
                contentDescription = item.title,
                size = 80.dp,
                cacheKey = item.id,
            )
        }
    }
}

@Preview
@Composable
private fun EpisodeListItemPreview() {
    ThmanyahTheme {
        Surface {
            EpisodeListItem(
                item = ContentItemSamples.episode(),
                onClick = {},
            )
        }
    }
}
