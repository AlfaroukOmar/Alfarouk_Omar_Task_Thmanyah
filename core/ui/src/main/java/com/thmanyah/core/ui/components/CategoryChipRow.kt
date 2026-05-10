package com.thmanyah.core.ui.components

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.thmanyah.core.designsystem.theme.ThmanyahDimens.spaceMd
import com.thmanyah.core.designsystem.theme.ThmanyahDimens.spaceSm
import com.thmanyah.core.designsystem.theme.ThmanyahTheme


data class CategoryChipUi(
    val id: String,
    val label: String,
)

@Composable
fun CategoryChipRow(
    chips: List<CategoryChipUi>,
    selectedId: String?,
    onSelected: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState())
            .padding(horizontal = spaceMd, vertical = spaceSm),
        horizontalArrangement = Arrangement.spacedBy(spaceSm),
    ) {
        chips.forEach { chip ->
            val selected = chip.id == selectedId
            FilterChip(
                selected = selected,
                onClick = { onSelected(chip.id) },
                label = {
                    Text(
                        text = chip.label,
                        style = MaterialTheme.typography.labelLarge,
                    )
                },
                colors = FilterChipDefaults.filterChipColors(
                    containerColor = MaterialTheme.colorScheme.surfaceContainerHigh,
                    labelColor = MaterialTheme.colorScheme.onSurface,
                    selectedContainerColor = MaterialTheme.colorScheme.primary,
                    selectedLabelColor = MaterialTheme.colorScheme.onPrimary,
                    iconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                ),
                border = FilterChipDefaults.filterChipBorder(
                    enabled = true,
                    selected = selected,
                    borderColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.35f),
                ),
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CategoryChipRowPreview() {
    val chips = listOf(
        CategoryChipUi("1", "All"),
        CategoryChipUi("2", "Podcasts"),
        CategoryChipUi("3", "Episodes"),
        CategoryChipUi("4", "Audio Books"),
        CategoryChipUi("5", "Articles")
    )
    ThmanyahTheme {
        CategoryChipRow(
            chips = chips,
            selectedId = "2",
            onSelected = {}
        )
    }
}
