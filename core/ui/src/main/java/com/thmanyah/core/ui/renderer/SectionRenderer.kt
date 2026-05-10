package com.thmanyah.core.ui.renderer

import androidx.compose.runtime.Composable
import com.thmanyah.domain.entities.ContentItem
import com.thmanyah.domain.entities.HomeSection
import com.thmanyah.domain.models.SectionLayout

fun interface SectionRenderer {
    @Composable
    operator fun invoke(
        section: HomeSection,
        onContentClick: (ContentItem) -> Unit,
    )
}

object SectionRendererRegistry {
    private val map = mutableMapOf(
        SectionLayout.Square to SquareRowRenderer,
        SectionLayout.TwoLinesGrid to TwoLinesGridRenderer,
        SectionLayout.BigSquare to BigSquareRowRenderer,
        SectionLayout.Queue to QueueRowRenderer,
        SectionLayout.Lounge to LoungeRenderer,
        SectionLayout.Unknown to UnknownLayoutRenderer,
    )

    operator fun get(layout: SectionLayout): SectionRenderer =
        map[layout] ?: UnknownLayoutRenderer

    fun register(layout: SectionLayout, renderer: SectionRenderer) {
        map[layout] = renderer
    }
}
