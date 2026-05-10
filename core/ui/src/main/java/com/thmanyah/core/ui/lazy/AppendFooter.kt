package com.thmanyah.core.ui.lazy

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.thmanyah.core.common.extensions.toUiText
import com.thmanyah.core.designsystem.theme.ThmanyahDimens
import com.thmanyah.core.ui.R
import com.thmanyah.core.ui.components.ErrorView
import com.thmanyah.core.ui.loading.AppendLoadingSectionShimmer
import com.thmanyah.domain.models.AppendLoadState

@Composable
fun AppendFooter(
    appendState: AppendLoadState,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier,
) {
    when (appendState) {
        AppendLoadState.Idle -> Unit
        AppendLoadState.Loading -> {
            AppendLoadingSectionShimmer(modifier = modifier)
        }
        is AppendLoadState.Error -> {
            ErrorView(
                message = appendState.error.toUiText(),
                onRetry = onRetry,
                retryLabel = stringResource(R.string.retry),
                modifier = modifier,
            )
        }
        AppendLoadState.EndReached -> {
            Text(
                text = stringResource(R.string.end_of_feed),
                modifier = modifier.padding(ThmanyahDimens.spaceMd),
            )
        }
    }
}
