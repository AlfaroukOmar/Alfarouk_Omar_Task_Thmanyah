package com.thmanyah.core.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.thmanyah.domain.entities.ContentItem
import androidx.compose.ui.platform.LocalResources
import com.thmanyah.core.designsystem.theme.ThmanyahDimens
import com.thmanyah.core.ui.R
import com.thmanyah.core.ui.model.displaySubtitleForLounge
import com.thmanyah.core.ui.model.releaseDateOrNull
import com.thmanyah.core.ui.util.formatDurationShort
import com.thmanyah.core.ui.util.formatRelativePublishDate

@Composable
fun LoungePostCard(
    item: ContentItem,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    onAttachmentClick: () -> Unit = onClick,
) {
    val res = LocalResources.current
    Card(
        modifier = modifier
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(ThmanyahDimens.cardCorner),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainerHigh),
    ) {
        Column(Modifier.padding(ThmanyahDimens.spaceMd)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                ContentImage(
                    imageUrl = item.imageUrl,
                    contentDescription = null,
                    size = 40.dp,
                    cacheKey = "${item.id}_avatar",
                    shape = CircleShape,
                )
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .padding(horizontal = ThmanyahDimens.spaceSm),
                ) {
                    Text(
                        text = item.displaySubtitleForLounge(),
                        style = MaterialTheme.typography.titleSmall,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )
                }
                val rel = res.formatRelativePublishDate(item.releaseDateOrNull())
                if (rel != null) {
                    Text(
                        text = rel,
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
            }
            Text(
                text = item.title,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(top = ThmanyahDimens.spaceSm),
                maxLines = 3,
                overflow = TextOverflow.Ellipsis,
            )
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = ThmanyahDimens.spaceMd)
                    .clickable { onAttachmentClick() },
                shape = RoundedCornerShape(ThmanyahDimens.cardCorner),
                color = MaterialTheme.colorScheme.surfaceContainerLow,
            ) {
                Row(
                    modifier = Modifier.padding(ThmanyahDimens.spaceMd),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    ContentImage(
                        imageUrl = item.imageUrl,
                        contentDescription = null,
                        size = 48.dp,
                        cacheKey = "${item.id}_attach",
                    )
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .padding(horizontal = ThmanyahDimens.spaceSm),
                    ) {
                        Text(
                            text = item.title,
                            style = MaterialTheme.typography.titleSmall,
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis,
                        )
                        val dur = res.formatDurationShort(item.durationSeconds)
                        if (dur != null) {
                            Text(
                                text = dur,
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                            )
                        }
                    }
                    IconButton(onClick = onAttachmentClick) {
                        Icon(
                            painter = painterResource(R.drawable.ic_play_filled_24),
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onSurface,
                        )
                    }
                }
            }
        }
    }
}