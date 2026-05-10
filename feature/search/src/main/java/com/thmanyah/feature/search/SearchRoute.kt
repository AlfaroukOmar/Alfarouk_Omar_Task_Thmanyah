package com.thmanyah.feature.search

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.displayCutout
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.thmanyah.core.designsystem.theme.ThmanyahDimens.spaceMd
import com.thmanyah.core.ui.components.EmptyStateView
import com.thmanyah.core.ui.components.ErrorView
import com.thmanyah.core.ui.loading.LoadingShimmerHome
import com.thmanyah.core.ui.renderer.SectionRendererRegistry
import kotlin.collections.isNotEmpty

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchRoute(
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: SearchViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val query by viewModel.queryForUi.collectAsStateWithLifecycle()
    val keyboard = LocalSoftwareKeyboardController.current

    Column(
        modifier = modifier
            .fillMaxSize()
            .navigationBarsPadding()
            .windowInsetsPadding(WindowInsets.displayCutout.only(WindowInsetsSides.Horizontal)),
    ) {
        TopAppBar(
            title = { Text(stringResource(R.string.search_title)) },
            navigationIcon = {
                IconButton(onClick = onBack) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = stringResource(R.string.search_back_cd))
                }
            },
        )
        OutlinedTextField(
            value = query,
            onValueChange = viewModel::onQueryChange,
            modifier = Modifier
                .fillMaxWidth()
                .padding(spaceMd),
            placeholder = { Text(stringResource(R.string.search_placeholder)) },
            leadingIcon = { Icon(Icons.Filled.Search, contentDescription = stringResource(R.string.search_icon_cd)) },
            trailingIcon = {
                if (query.isNotEmpty()) {
                    IconButton(onClick = viewModel::clearQuery) {
                        Icon(Icons.Default.Clear, contentDescription = stringResource(R.string.search_clear_cd))
                    }
                }
            },
            singleLine = true,
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
            keyboardActions = KeyboardActions(
                onSearch = { keyboard?.hide() },
            ),
        )

        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .imePadding(),
        ) {
            when (val s = state) {
                SearchUiState.Idle -> EmptyStateView(title = stringResource(R.string.search_idle_title))
                is SearchUiState.Loading -> {
                    if (s.previous.isEmpty()) {
                        LoadingShimmerHome()
                    } else {
                        LazyColumn {
                            itemsIndexed(
                                s.previous,
                                key = { index, section -> "${section.id}:$index" },
                            ) { _, section ->
                                SectionRendererRegistry[section.layout](section, onContentClick = {})
                            }
                        }
                    }
                }

                is SearchUiState.Success -> LazyColumn {
                    itemsIndexed(
                        s.results,
                        key = { index, section -> "${section.id}:$index" },
                    ) { _, section ->
                        SectionRendererRegistry[section.layout](section, onContentClick = {})
                    }
                }

                is SearchUiState.Empty -> EmptyStateView(title = stringResource(R.string.search_no_results))

                is SearchUiState.Error -> {
                    if (s.previous.isNotEmpty()) {
                        LazyColumn {
                            itemsIndexed(
                                s.previous,
                                key = { index, section -> "${section.id}:$index" },
                            ) { _, section ->
                                SectionRendererRegistry[section.layout](section, onContentClick = {})
                            }
                        }
                    }
                    ErrorView(
                        message = s.message,
                        onRetry = viewModel::retry,
                        retryLabel = stringResource(com.thmanyah.core.ui.R.string.retry),
                    )
                }
            }
        }
    }
}
