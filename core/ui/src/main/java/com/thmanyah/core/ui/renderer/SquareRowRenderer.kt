package com.thmanyah.core.ui.renderer

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.thmanyah.core.designsystem.theme.ThmanyahDimens
import com.thmanyah.core.ui.components.ContentImage
import com.thmanyah.core.ui.components.SectionTitleRow
import com.thmanyah.domain.entities.ContentItem
import com.thmanyah.domain.entities.HomeSection

object SquareRowRenderer : SectionRenderer {
    @Composable
    override operator fun invoke(section: HomeSection, onContentClick: (ContentItem) -> Unit) {
        Column(modifier = Modifier.padding(bottom = ThmanyahDimens.spaceMd)) {
            SectionTitleRow(title = section.title)
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(ThmanyahDimens.spaceSm),
                modifier = Modifier.padding(horizontal = ThmanyahDimens.spaceMd),
            ) {
                itemsIndexed(
                    section.items,
                    key = { index, item -> "${section.id}:${item.id}:$index" },
                ) { _, item ->
                    Column(
                        modifier = Modifier
                            .width(120.dp)
                            .clickable { onContentClick(item) },
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
                        ContentImage(
                            imageUrl = item.imageUrl,
                            contentDescription = item.title,
                            size = 120.dp,
                            cacheKey = item.id,
                        )
                        Text(
                            text = item.title,
                            style = MaterialTheme.typography.bodySmall,
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis,
                            modifier = Modifier.padding(top = ThmanyahDimens.spaceSm),
                        )
                    }
                }
            }
        }
    }
}
