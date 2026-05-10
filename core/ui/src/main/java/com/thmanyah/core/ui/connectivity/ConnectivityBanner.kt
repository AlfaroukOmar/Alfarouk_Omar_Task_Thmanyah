package com.thmanyah.core.ui.connectivity

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.thmanyah.core.designsystem.theme.ThmanyahDimens

@Composable
fun ConnectivityBanner(
    isOffline: Boolean,
    message: String,
    modifier: Modifier = Modifier,
) {
    AnimatedVisibility(visible = isOffline) {
        Surface(
            color = MaterialTheme.colorScheme.errorContainer,
            tonalElevation = 2.dp,
            modifier = modifier.fillMaxWidth(),
        ) {
            Text(
                text = message,
                modifier = Modifier.padding(ThmanyahDimens.spaceMd),
                color = MaterialTheme.colorScheme.onErrorContainer,
                style = MaterialTheme.typography.bodyMedium,
            )
        }
    }
}