package com.thmanyah.feature.setting.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import com.thmanyah.core.designsystem.theme.ThmanyahDimens.cardCorner
import com.thmanyah.core.designsystem.theme.ThmanyahDimens.spaceSm
import com.thmanyah.domain.prefs.DarkModeOption

@Composable
fun ThemeSegmentedSwitcher(
    selected: DarkModeOption,
    onSelect: (DarkModeOption) -> Unit,
    modifier: Modifier = Modifier,
    labels: @Composable (DarkModeOption) -> String,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .selectableGroup(),
        horizontalArrangement = Arrangement.spacedBy(spaceSm),
    ) {
        for (mode in listOf(
            DarkModeOption.System,
            DarkModeOption.Light,
            DarkModeOption.Dark,
        )) {
            FilterChip(
                selected = selected == mode,
                onClick = { onSelect(mode) },
                label = { Text(labels(mode), style = MaterialTheme.typography.labelLarge) },
                shape = RoundedCornerShape(cardCorner),
                modifier = Modifier
                    .weight(1f)
                    .semantics { role = Role.RadioButton },
            )
        }
    }
}
