package com.thmanyah.core.ui.renderer

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.thmanyah.core.designsystem.theme.ThmanyahDimens
import com.thmanyah.domain.entities.ContentItem
import com.thmanyah.domain.entities.HomeSection
import timber.log.Timber
import java.util.Collections
import androidx.compose.ui.platform.LocalResources
import com.thmanyah.core.ui.components.ContentImage
import com.thmanyah.core.ui.components.SectionTitleRow
import com.thmanyah.core.ui.util.formatDurationShort

private val loggedUnknownLayouts = Collections.synchronizedSet(HashSet<String>())

object UnknownLayoutRenderer : SectionRenderer {
    @Composable
    override operator fun invoke(section: HomeSection, onContentClick: (ContentItem) -> Unit) {
        val key = section.title + section.layout
        if (loggedUnknownLayouts.add(key)) {
            Timber.d("Unknown/fallback layout rendering for ${section.layout} / ${section.title}")
        }
        val res = LocalResources.current
        Column(modifier = Modifier.padding(bottom = ThmanyahDimens.spaceMd)) {
            SectionTitleRow(title = section.displayTitle())
            BoxWithConstraints(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = ThmanyahDimens.spaceMd),
            ) {
                val itemWidth = maxWidth * 0.88f
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(ThmanyahDimens.spaceSm),
                ) {
                    itemsIndexed(
                        section.items,
                        key = { index, item -> "${section.id}:${item.id}:$index" },
                    ) { _, item ->
                        Row(
                            modifier = Modifier
                                .width(itemWidth)
                                .clickable { onContentClick(item) }
                                .padding(vertical = ThmanyahDimens.spaceSm),
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            ContentImage(
                                imageUrl = item.imageUrl,
                                contentDescription = item.title,
                                size = 56.dp,
                                cacheKey = item.id,
                            )
                            Column(Modifier.padding(start = ThmanyahDimens.spaceSm)) {
                                Text(item.title, style = MaterialTheme.typography.bodyLarge)
                                val d = res.formatDurationShort(item.durationSeconds)
                                if (d != null) {
                                    Text(d, style = MaterialTheme.typography.labelSmall)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
