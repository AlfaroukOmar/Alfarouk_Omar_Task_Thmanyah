package com.thmanyah.core.ui.renderer

import android.content.res.Resources
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.thmanyah.domain.entities.ContentItem
import com.thmanyah.domain.entities.HomeSection
import androidx.compose.ui.platform.LocalResources
import com.thmanyah.core.designsystem.theme.ThmanyahDimens
import com.thmanyah.core.ui.R
import com.thmanyah.core.ui.components.EpisodeListItem
import com.thmanyah.core.ui.components.SectionTitleRow
import com.thmanyah.core.ui.util.formatDurationShort


private val twoLinesGridCellWidth = 300.dp
private val episodeRowHeight = 100.dp

object TwoLinesGridRenderer : SectionRenderer {
    @Composable
    override operator fun invoke(section: HomeSection, onContentClick: (ContentItem) -> Unit) {
        val res = LocalResources.current
        val trailing = sectionSummaryTrailing(section, res)
        val rowCount = if (section.items.size == 1) 1 else 2
        val gridHeight = when {
            section.items.isEmpty() -> 0.dp
            section.items.size == 1 -> episodeRowHeight
            else -> episodeRowHeight * 2 + ThmanyahDimens.spaceSm
        }
        Column(modifier = Modifier.padding(bottom = ThmanyahDimens.spaceMd)) {
            SectionTitleRow(title = section.displayTitle(), trailingText = trailing)
            if (section.items.isNotEmpty()) {
                LazyHorizontalGrid(
                    rows = GridCells.Fixed(rowCount),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(gridHeight)
                        .padding(horizontal = ThmanyahDimens.spaceMd),
                    horizontalArrangement = Arrangement.spacedBy(ThmanyahDimens.spaceSm),
                    verticalArrangement = Arrangement.spacedBy(ThmanyahDimens.spaceSm),
                ) {
                    itemsIndexed(
                        section.items,
                        key = { index, item -> "${section.id}:${item.id}:$index" },
                    ) { _, item ->
                        EpisodeListItem(
                            item = item,
                            onClick = { onContentClick(item) },
                            modifier = Modifier.width(twoLinesGridCellWidth),
                        )
                    }
                }
            }
        }
    }
}

private fun sectionSummaryTrailing(section: HomeSection, res: Resources): String? {
    if (section.items.isEmpty()) return null
    val n = section.items.size
    val totalSec = section.items.sumOf { it.durationSeconds ?: 0L }
    val countStr = res.getQuantityString(R.plurals.episodes_count_label, n, n)
    val dur = if (totalSec > 0) res.formatDurationShort(totalSec) else null
    return if (dur != null) {
        countStr + res.getString(R.string.meta_separator) + dur
    } else {
        countStr
    }
}
