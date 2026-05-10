package com.thmanyah.core.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import com.thmanyah.core.designsystem.theme.ThmanyahDimens
import com.thmanyah.core.ui.R
import com.thmanyah.core.ui.model.releaseDateOrNull
import com.thmanyah.core.ui.util.formatDurationShort
import com.thmanyah.core.ui.util.formatRelativePublishDate
import com.thmanyah.domain.entities.ContentItem
import com.thmanyah.domain.entities.HomeSection
import kotlin.collections.get


private val QueueHeroCardShape = RoundedCornerShape(18.dp)
private val QueueHeroMainHeight = 240.dp
private const val HeroParallaxFactor = 0.22f

@Composable
fun QueueSectionPagerCard(
    section: HomeSection,
    onContentClick: (ContentItem) -> Unit,
    modifier: Modifier = Modifier,
) {
    val items = section.items
    if (items.isEmpty()) return

    var currentIndex by remember { mutableIntStateOf(0) }
    LaunchedEffect(section.id) {
        currentIndex = 0
    }

    val dragPx = remember { mutableFloatStateOf(0f) }
    val currentItem = items[currentIndex]

    Card(
        modifier = modifier.fillMaxWidth(),
        shape = QueueHeroCardShape,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainerHigh),
    ) {
        CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Ltr) {
            Column(modifier = Modifier.fillMaxWidth()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(QueueHeroMainHeight)
                        .clip(QueueHeroCardShape),
                ) {
                    ContentImageHero(
                        imageUrl = currentItem.imageUrl,
                        contentDescription = currentItem.title,
                        modifier = Modifier.fillMaxSize(),
                        translationX = dragPx.floatValue * HeroParallaxFactor,
                        cacheKey = "${section.id}_hero_${currentItem.id}",
                    )

                    if (items.size > 1) {
                        QueueCardDeckPeekRail(
                            items = items,
                            currentIndex = currentIndex,
                            cacheKeyPrefix = "${section.id}_idx$currentIndex",
                            dragOffsetPx = dragPx.floatValue,
                            modifier = Modifier
                                .fillMaxSize()
                                .align(Alignment.CenterEnd),
                        )
                    }

                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(
                                Brush.horizontalGradient(
                                    colorStops = arrayOf(
                                        0f to Color.Black.copy(alpha = 0.78f),
                                        0.52f to Color.Black.copy(alpha = 0.28f),
                                        0.82f to Color.Transparent,
                                        1f to Color.Transparent,
                                    ),
                                ),
                            ),
                    )
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(
                                Brush.verticalGradient(
                                    colorStops = arrayOf(
                                        0f to Color.Transparent,
                                        0.58f to Color.Transparent,
                                        1f to Color.Black.copy(alpha = 0.42f),
                                    ),
                                ),
                            ),
                    )

                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(ThmanyahDimens.spaceMd)
                            .pointerInput(currentIndex) {
                                detectTapGestures(
                                    onTap = { onContentClick(items[currentIndex]) },
                                )
                            }
                            .pointerInput(items.size, currentIndex, section.id) {
                                val maxShift = size.width * 0.35f
                                val threshold = size.width * 0.12f
                                detectHorizontalDragGestures(
                                    onHorizontalDrag = { _, dx ->
                                        dragPx.floatValue = (dragPx.floatValue + dx)
                                            .coerceIn(-maxShift, maxShift)
                                    },
                                    onDragCancel = { dragPx.floatValue = 0f },
                                    onDragEnd = {
                                        val shift = dragPx.floatValue
                                        if (items.size > 1) {
                                            when {
                                                shift < -threshold -> {
                                                    currentIndex = (currentIndex + 1) % items.size
                                                }
                                                shift > threshold -> {
                                                    currentIndex = (currentIndex - 1 + items.size) % items.size
                                                }
                                            }
                                        }
                                        dragPx.floatValue = 0f
                                    },
                                )
                            },
                        verticalArrangement = Arrangement.SpaceBetween,
                    ) {
                        QueueItemPageRowBody(
                            item = currentItem,
                            modifier = Modifier.fillMaxWidth(),
                            contentOnDarkScrim = true,
                        )
                        if (items.size > 1) {
                            QueuePagerDots(
                                pageCount = items.size,
                                selectedPage = currentIndex,
                                useLightStyle = true,
                                modifier = Modifier.fillMaxWidth(),
                            )
                        } else {
                            Spacer(modifier = Modifier.height(8.dp))
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun QueueItemPageRowBody(
    item: ContentItem,
    modifier: Modifier = Modifier,
    contentOnDarkScrim: Boolean = false,
) {
    val res = LocalContext.current.resources
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.SpaceBetween,
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(ThmanyahDimens.spaceXs)) {
            Text(
                text = item.title,
                style = MaterialTheme.typography.titleSmall,
                color = if (contentOnDarkScrim) {
                    Color.White
                } else {
                    MaterialTheme.colorScheme.onSurface
                },
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
            )
            QueueMetadataLine(
                item = item,
                res = res,
                onDarkScrim = contentOnDarkScrim,
            )
        }
        QueuePlayChip(contentColor = Color.Black, containerColor = Color.White)
    }
}

@Composable
private fun QueueMetadataLine(
    item: ContentItem,
    res: android.content.res.Resources,
    onDarkScrim: Boolean = false,
) {
    val dur = res.formatDurationShort(item.durationSeconds)
    val rel = res.formatRelativePublishDate(item.releaseDateOrNull())
    val sep = res.getString(R.string.meta_separator)
    val primary = if (onDarkScrim) Color.White else MaterialTheme.colorScheme.primary
    val muted = if (onDarkScrim) Color.White.copy(alpha = 0.78f) else MaterialTheme.colorScheme.onSurfaceVariant
    val text = buildAnnotatedString {
        if (dur != null) {
            pushStyle(SpanStyle(color = primary))
            append(dur)
            pop()
        }
        if (dur != null && rel != null) {
            append(sep)
        }
        if (rel != null) {
            pushStyle(SpanStyle(color = muted))
            append(rel)
            pop()
        }
    }
    if (text.isNotEmpty()) {
        Text(
            text = text,
            style = MaterialTheme.typography.labelSmall,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
        )
    }
}
