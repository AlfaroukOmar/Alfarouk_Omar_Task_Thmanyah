package com.thmanyah.core.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.Dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.thmanyah.core.designsystem.theme.ThmanyahDimens.cardCorner
import com.thmanyah.core.designsystem.theme.ThmanyahDimens.spaceMd


@Composable
fun ContentImageHero(
    imageUrl: String?,
    contentDescription: String?,
    modifier: Modifier = Modifier,
    translationX: Float = 0f,
    cacheKey: String? = null,
) {
    val ctx = LocalContext.current
    val request = ImageRequest.Builder(ctx)
        .data(imageUrl)
        .crossfade(true)
        .apply {
            if (cacheKey != null) {
                memoryCacheKey(cacheKey)
                diskCacheKey(cacheKey)
            }
        }
        .build()
    Box(
        modifier = modifier
            .background(MaterialTheme.colorScheme.surfaceContainerHigh)
            .graphicsLayer { this.translationX = translationX },
    ) {
        AsyncImage(
            model = request,
            contentDescription = contentDescription,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize(),
        )
    }
}

@Composable
fun ContentImage(
    imageUrl: String?,
    contentDescription: String?,
    modifier: Modifier = Modifier,
    size: Dp = spaceMd * 5,
    cacheKey: String? = null,
    shape: Shape = RoundedCornerShape(cardCorner),
) {
    val ctx = LocalContext.current
    val request = ImageRequest.Builder(ctx)
        .data(imageUrl)
        .crossfade(true)
        .apply {
            if (cacheKey != null) {
                memoryCacheKey(cacheKey)
                diskCacheKey(cacheKey)
            }
        }
        .build()
    Box(
        modifier = modifier
            .size(size)
            .clip(shape)
            .background(MaterialTheme.colorScheme.surfaceContainerHigh),
    ) {
        AsyncImage(
            model = request,
            contentDescription = contentDescription,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize(),
        )
    }
}