package com.thmanyah.core.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.thmanyah.core.designsystem.theme.ThmanyahTheme
import com.thmanyah.core.designsystem.theme.ThmanyahDimens.spaceLg
import com.thmanyah.core.designsystem.theme.ThmanyahDimens.spaceSm

@Composable
fun EmptyStateView(
    title: String,
    modifier: Modifier = Modifier,
    subtitle: String? = null,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(spaceLg),
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center,
        )
        if (subtitle != null) {
            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = spaceSm),
                textAlign = TextAlign.Center,
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun EmptyStateViewPreview() {
    ThmanyahTheme {
        EmptyStateView(
            title = "لا يوجد محتوى",
            subtitle = "جرب البحث عن شيء آخر أو العودة لاحقاً",
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun EmptyStateViewOnlyTitlePreview() {
    ThmanyahTheme {
        EmptyStateView(
            title = "لا يوجد محتوى",
        )
    }
}
