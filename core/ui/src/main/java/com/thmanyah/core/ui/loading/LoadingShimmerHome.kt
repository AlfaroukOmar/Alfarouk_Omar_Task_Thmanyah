package com.thmanyah.core.ui.loading

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.thmanyah.core.designsystem.theme.ThmanyahDimens

@Composable
fun LoadingShimmerHome(modifier: Modifier = Modifier) {
    LazyColumn(
        modifier = modifier
            .fillMaxWidth()
            .padding(bottom = ThmanyahDimens.spaceMd),
    ) {
        itemsIndexed(
            LoadingShimmerLayoutSequence,
            key = { index, layout -> "shimmer:${layout.ordinal}:$index" },
        ) { _, layout ->
            SectionShimmerRegistry[layout]()
        }
    }
}
