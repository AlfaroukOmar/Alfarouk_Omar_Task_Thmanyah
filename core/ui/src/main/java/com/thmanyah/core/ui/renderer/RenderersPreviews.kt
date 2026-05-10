package com.thmanyah.core.ui.renderer

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import com.thmanyah.core.fixtures.HomeSectionSamples
import com.thmanyah.core.ui.preview.LightDarkPreview
import com.thmanyah.core.ui.preview.ThmanyahPreviewSurface


@LightDarkPreview
@Composable
fun SquareRowRendererPreview() {
    ThmanyahPreviewSurface (darkTheme = isSystemInDarkTheme()) {
        SquareRowRenderer(HomeSectionSamples.squareSection(), onContentClick = {})
    }
}

@LightDarkPreview
@Composable
fun TwoLinesGridRendererPreview() {
    ThmanyahPreviewSurface(darkTheme = isSystemInDarkTheme()) {
        TwoLinesGridRenderer(HomeSectionSamples.twoLinesGridSection(), onContentClick = {})
    }
}

@LightDarkPreview
@Composable
fun BigSquareRowRendererPreview() {
    ThmanyahPreviewSurface(darkTheme = isSystemInDarkTheme()) {
        BigSquareRowRenderer(HomeSectionSamples.bigSquareSection(), onContentClick = {})
    }
}

@LightDarkPreview
@Composable
fun QueueRowRendererPreview() {
    ThmanyahPreviewSurface(darkTheme = isSystemInDarkTheme()) {
        QueueRowRenderer(HomeSectionSamples.queueSection(), onContentClick = {})
    }
}

@LightDarkPreview
@Composable
fun LoungeRendererPreview() {
    ThmanyahPreviewSurface(darkTheme = isSystemInDarkTheme()) {
        LoungeRenderer(HomeSectionSamples.loungeSection(), onContentClick = {})
    }
}

@LightDarkPreview
@Composable
fun UnknownLayoutRendererPreview() {
    ThmanyahPreviewSurface(darkTheme = isSystemInDarkTheme()) {
        UnknownLayoutRenderer(HomeSectionSamples.unknownSection(), onContentClick = {})
    }
}
