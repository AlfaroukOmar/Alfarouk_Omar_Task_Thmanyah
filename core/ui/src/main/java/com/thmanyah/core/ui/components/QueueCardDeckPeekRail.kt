package com.thmanyah.core.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.thmanyah.domain.entities.ContentItem

private val PeekCardShape = RoundedCornerShape(10.dp)

@Composable
fun QueueCardDeckPeekRail(
    items: List<ContentItem>,
    currentIndex: Int,
    cacheKeyPrefix: String,
    dragOffsetPx: Float,
    modifier: Modifier = Modifier,
    cardWidth: Dp = 62.dp,
    cardHeight: Dp = 118.dp,
) {
    val ctx = LocalContext.current
    val n = items.size
    if (n <= 1) return

    val peekIndices = (1..2)
        .map { (currentIndex + it) % n }
        .distinct()
        .filter { it != currentIndex }
        .take(2)
    if (peekIndices.isEmpty()) return

    val orderedBackToFront = peekIndices.reversed()

    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.CenterEnd,
    ) {
        Box(
            modifier = Modifier
                .wrapContentSize(align = Alignment.CenterEnd)
                .padding(end = 14.dp),
        ) {
            orderedBackToFront.forEachIndexed { stackPos, itemIndex ->
                val item = items[itemIndex]
                val depthFromFront = orderedBackToFront.size - stackPos
                val rotation = 3f + depthFromFront * 7f
                val offsetX = (8 + depthFromFront * 14).dp
                val scale = 1f - (depthFromFront - 1) * 0.06f
                val alpha = (0.9f - depthFromFront * 0.14f).coerceIn(0.5f, 1f)
                val desc = "Next in queue: ${item.title}"
                val cacheKey = "${cacheKeyPrefix}_peek_${itemIndex}_${item.id}"
                val request = ImageRequest.Builder(ctx)
                    .data(item.imageUrl)
                    .crossfade(true)
                    .memoryCacheKey(cacheKey)
                    .diskCacheKey(cacheKey)
                    .build()
                Box(
                    modifier = Modifier
                        .align(Alignment.CenterEnd)
                        .offset(x = offsetX)
                        .graphicsLayer {
                            rotationZ = rotation
                            scaleX = scale
                            scaleY = scale
                            this.alpha = alpha
                            translationX = dragOffsetPx * 0.12f
                        },
                ) {
                    Surface(
                        shape = PeekCardShape,
                        shadowElevation = 8.dp,
                        color = MaterialTheme.colorScheme.surfaceContainerHigh,
                        modifier = Modifier
                            .width(cardWidth)
                            .height(cardHeight),
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .border(1.dp, Color.White.copy(alpha = 0.38f), PeekCardShape),
                        ) {
                            AsyncImage(
                                model = request,
                                contentDescription = desc,
                                contentScale = ContentScale.Crop,
                                modifier = Modifier.fillMaxSize().clip(PeekCardShape),
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
internal fun QueuePagerDots(
    pageCount: Int,
    selectedPage: Int,
    modifier: Modifier = Modifier,
    useLightStyle: Boolean = false,
) {
    if (pageCount <= 1) return
    val selectedColor = if (useLightStyle) Color.White else MaterialTheme.colorScheme.primary
    val unselectedColor = if (useLightStyle) {
        Color.White.copy(alpha = 0.45f)
    } else {
        MaterialTheme.colorScheme.outline.copy(alpha = 0.45f)
    }
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(bottom = 8.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        repeat(pageCount) { index ->
            Box(
                modifier = Modifier
                    .padding(horizontal = 3.dp)
                    .size(6.dp)
                    .clip(CircleShape)
                    .background(
                        if (index == selectedPage) selectedColor else unselectedColor,
                    ),
            )
        }
    }
}
