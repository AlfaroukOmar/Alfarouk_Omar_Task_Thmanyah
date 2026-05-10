package com.thmanyah.core.ui.components

import androidx.annotation.DrawableRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.thmanyah.core.designsystem.theme.ThmanyahDimens


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ThmanyahTopBar(
    greetingLine: String,
    modifier: Modifier = Modifier,
    @DrawableRes avatarUrl: Int? = null,
    @DrawableRes avatarPlaceholderRes: Int? = null,
    avatarContentDescription: String? = null,
    settingsContentDescription: String? = null,
    searchContentDescription: String? = null,
    onOpenSettings: () -> Unit = {},
    onSearchClick: (() -> Unit)? = null,
) {
    TopAppBar(
        modifier = modifier,
        title = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(ThmanyahDimens.spaceSm),
            ) {
                val avatarSize = ThmanyahDimens.spaceMd * 2
                when {
                    avatarUrl != null -> {
                        AsyncImage(
                            model = ImageRequest.Builder(LocalContext.current).data(avatarUrl).crossfade(true).build(),
                            contentDescription = avatarContentDescription,
                            modifier = Modifier
                                .size(avatarSize)
                                .clip(CircleShape),
                            contentScale = ContentScale.Fit,
                        )
                    }
                    avatarPlaceholderRes != null -> {
                        AsyncImage(
                            model = ImageRequest.Builder(LocalContext.current).data(avatarPlaceholderRes).crossfade(true).build(),
                            contentDescription = avatarContentDescription,
                            modifier = Modifier
                                .size(avatarSize)
                                .clip(CircleShape),
                            contentScale = ContentScale.Crop,
                        )
                    }
                    else -> {
                        Box(
                            modifier = Modifier
                                .size(avatarSize)
                                .clip(CircleShape)
                                .background(MaterialTheme.colorScheme.surfaceContainerHigh),
                        )
                    }
                }
                Text(
                    text = greetingLine,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface,
                )
            }
        },
        actions = {
            IconButton(onClick = onOpenSettings) {
                Icon(
                    imageVector = Icons.Default.Settings,
                    contentDescription = settingsContentDescription,
                )
            }
            if (onSearchClick != null) {
                IconButton(onClick = onSearchClick) {
                    Icon(
                        imageVector = Icons.Filled.Search,
                        contentDescription = searchContentDescription,
                    )
                }
            }
        },
    )
}