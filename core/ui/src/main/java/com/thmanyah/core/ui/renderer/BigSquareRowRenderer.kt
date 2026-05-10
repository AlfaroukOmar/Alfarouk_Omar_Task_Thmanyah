package com.thmanyah.core.ui.renderer

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.thmanyah.core.designsystem.theme.ThmanyahDimens
import com.thmanyah.core.ui.components.BigSquareCarouselCard
import com.thmanyah.core.ui.components.SectionTitleRow
import com.thmanyah.domain.entities.ContentItem
import com.thmanyah.domain.entities.HomeSection

object BigSquareRowRenderer : SectionRenderer {
    @Composable
    override operator fun invoke(section: HomeSection, onContentClick: (ContentItem) -> Unit) {
        Column(modifier = Modifier.padding(bottom = ThmanyahDimens.spaceMd)) {
            SectionTitleRow(title = section.displayTitle())
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(ThmanyahDimens.spaceSm),
                modifier = Modifier.padding(horizontal = ThmanyahDimens.spaceMd),
            ) {
                itemsIndexed(
                    section.items,
                    key = { index, item -> "${section.id}:${item.id}:$index" },
                ) { _, item ->
                    BigSquareCarouselCard(
                        item = item,
                        onClick = { onContentClick(item) },
                    )
                }
            }
        }
    }
}
