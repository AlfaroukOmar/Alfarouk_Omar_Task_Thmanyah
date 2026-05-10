package com.thmanyah.core.ui.renderer

import android.content.res.Resources
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.thmanyah.core.designsystem.theme.ThmanyahDimens
import com.thmanyah.core.ui.R
import com.thmanyah.core.ui.components.QueueSectionPagerCard
import com.thmanyah.core.ui.components.SectionTitleRow
import com.thmanyah.core.ui.util.formatDurationShort
import com.thmanyah.domain.entities.ContentItem
import com.thmanyah.domain.entities.HomeSection


object QueueRowRenderer : SectionRenderer {
    @Composable
    override operator fun invoke(section: HomeSection, onContentClick: (ContentItem) -> Unit) {
        val res = LocalContext.current.resources
        val trailing = queueSectionTrailing(section, res)
        Column(modifier = Modifier.padding(bottom = ThmanyahDimens.spaceMd)) {
            SectionTitleRow(title = section.displayTitle(), trailingText = trailing)
            if (section.items.isNotEmpty()) {
                QueueSectionPagerCard(
                    section = section,
                    onContentClick = onContentClick,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = ThmanyahDimens.spaceMd),
                )
            }
        }
    }
}

private fun queueSectionTrailing(section: HomeSection, res: Resources): String? {
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
