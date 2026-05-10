package com.thmanyah.core.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ErrorOutline
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.thmanyah.core.common.extensions.UiText
import com.thmanyah.core.designsystem.theme.ThmanyahTheme
import com.thmanyah.core.designsystem.theme.ThmanyahDimens

import com.thmanyah.core.ui.text.resolveString

@Composable
fun ErrorView(
    message: UiText,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier,
    retryLabel: String,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(ThmanyahDimens.spaceMd),
        verticalArrangement = Arrangement.spacedBy(ThmanyahDimens.spaceMd),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Icon(
            imageVector = Icons.Default.ErrorOutline,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.error,
        )
        Text(text = message.resolveString(), style = MaterialTheme.typography.bodyMedium)
        Button(onClick = onRetry) {
            Text(retryLabel)
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ErrorViewPreview() {
    ThmanyahTheme {
        ErrorView(
            message = UiText.Dynamic("An error occurred while loading the data. Please try again."),
            onRetry = {},
            retryLabel = "Retry",
        )
    }
}
