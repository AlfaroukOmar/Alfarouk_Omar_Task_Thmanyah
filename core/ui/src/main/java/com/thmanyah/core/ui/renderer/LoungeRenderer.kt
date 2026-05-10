package com.thmanyah.core.ui.renderer

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.thmanyah.core.designsystem.theme.ThmanyahDimens
import com.thmanyah.core.ui.components.LoungePostCard
import com.thmanyah.core.ui.components.SectionTitleRow
import com.thmanyah.domain.entities.ContentItem
import com.thmanyah.domain.entities.HomeSection


object LoungeRenderer : SectionRenderer {
    @Composable
    override operator fun invoke(section: HomeSection, onContentClick: (ContentItem) -> Unit) {
        Column(modifier = Modifier.padding(bottom = ThmanyahDimens.spaceMd)) {
            SectionTitleRow(title = section.displayTitle())
            BoxWithConstraints(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = ThmanyahDimens.spaceMd),
            ) {
                val cardWidth = maxWidth * 0.88f
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(ThmanyahDimens.spaceSm),
                ) {
                    itemsIndexed(
                        section.items,
                        key = { index, item -> "${section.id}:${item.id}:$index" },
                    ) { _, item ->
                        LoungePostCard(
                            item = item,
                            onClick = { onContentClick(item) },
                            modifier = Modifier.width(cardWidth),
                        )
                    }
                }
            }
        }
    }
}
