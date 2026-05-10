package com.thmanyah.feature.home

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.displayCutout
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.thmanyah.core.common.extensions.toUiText
import com.thmanyah.core.designsystem.theme.ThmanyahDimens.spaceMd
import com.thmanyah.core.ui.components.CategoryChipRow
import com.thmanyah.core.ui.components.CategoryChipUi
import com.thmanyah.core.ui.components.EmptyStateView
import com.thmanyah.core.ui.components.ErrorView
import com.thmanyah.core.ui.components.ThmanyahTopBar
import com.thmanyah.core.ui.connectivity.ConnectivityBanner
import com.thmanyah.core.ui.lazy.AppendFooter
import com.thmanyah.core.ui.loading.LoadingShimmerHome
import com.thmanyah.core.ui.renderer.SectionRendererRegistry
import com.thmanyah.domain.entities.HomeSection
import com.thmanyah.domain.models.AppendLoadState
import com.thmanyah.domain.models.ContentType
import com.thmanyah.domain.models.HomeFeedState
import com.thmanyah.domain.models.InitialLoadState
import kotlinx.coroutines.flow.distinctUntilChanged

@Composable
fun HomeRoute(
    onOpenSearch: () -> Unit,
    onOpenSettings: () -> Unit,
    modifier: Modifier = Modifier,
    homeViewModel: HomeViewModel = hiltViewModel(),
) {
    val feed by homeViewModel.feed.collectAsStateWithLifecycle()
    val isOffline by homeViewModel.isOffline.collectAsStateWithLifecycle()
    val selectedFilter by homeViewModel.selectedFilterKey.collectAsStateWithLifecycle()
    val chipModels by homeViewModel.categoryChips.collectAsStateWithLifecycle()
    val visibleSections by homeViewModel.visibleSections.collectAsStateWithLifecycle()

    HomeContent(
        feed = feed,
        isOffline = isOffline,
        selectedFilter = selectedFilter,
        chipModels = chipModels,
        visibleSections = visibleSections,
        onOpenSearch = onOpenSearch,
        onOpenSettings = onOpenSettings,
        onRefresh = homeViewModel::onRefresh,
        onFilterSelected = homeViewModel::onFilterSelected,
        onRetryInitial = homeViewModel::onRetryInitial,
        onAppendNearEnd = homeViewModel::onAppendNearEnd,
        onContentClick = homeViewModel::onContentClick,
        onAppendRetry = homeViewModel::onAppendRetry,
        modifier = modifier,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun HomeContent(
    feed: HomeFeedState,
    isOffline: Boolean,
    selectedFilter: String,
    chipModels: List<CategoryChipModel>,
    visibleSections: List<HomeSection>,
    onOpenSearch: () -> Unit,
    onOpenSettings: () -> Unit,
    onRefresh: () -> Unit,
    onFilterSelected: (String) -> Unit,
    onRetryInitial: () -> Unit,
    onAppendNearEnd: () -> Unit,
    onContentClick: () -> Unit,
    onAppendRetry: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val listState = rememberLazyListState()
    LaunchedEffect(listState, feed.appendState, visibleSections.size, feed.initialState) {
        snapshotFlow {
            val info = listState.layoutInfo
            val last = info.visibleItemsInfo.lastOrNull()?.index ?: -1
            val total = info.totalItemsCount
            last to total
        }
            .distinctUntilChanged()
            .collect { (last, total) ->
                if (feed.initialState == InitialLoadState.Ready &&
                    feed.appendState == AppendLoadState.Idle &&
                    total > 2 &&
                    last >= total - 2
                ) {
                    onAppendNearEnd()
                }
            }
    }

    val chips = chipModels.map { model ->
        CategoryChipUi(
            id = model.id,
            label = chipLabel(model),
        )
    }
    val greeting = stringResource(
        R.string.greeting_evening_named,
        stringResource(R.string.home_display_name),
    )
    val pullState = rememberPullToRefreshState()

    Scaffold(
        modifier = modifier.testTag("home_route"),
        topBar = {
            Column(modifier = Modifier.statusBarsPadding()) {
                ConnectivityBanner(isOffline = isOffline, message = stringResource(R.string.offline_banner))
                ThmanyahTopBar(
                    greetingLine = greeting,
                    avatarUrl = R.drawable.ic_alfarouk_profile_pic,
                    avatarPlaceholderRes = R.drawable.ic_profile_place_holder,
                    avatarContentDescription = stringResource(R.string.avatar_content_description),
                    settingsContentDescription = stringResource(R.string.home_settings_cd),
                    searchContentDescription = stringResource(R.string.home_search_cd),
                    onOpenSettings = onOpenSettings,
                    onSearchClick = onOpenSearch,
                )
            }
        },
    ) { paddingValues ->
        PullToRefreshBox(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .windowInsetsPadding(WindowInsets.displayCutout.only(WindowInsetsSides.Horizontal)),
            isRefreshing = feed.initialState == InitialLoadState.Loading,
            onRefresh = onRefresh,
            state = pullState,
        ) {
            Column {
                CategoryChipRow(
                    chips = chips,
                    selectedId = selectedFilter,
                    onSelected = onFilterSelected,
                )
                when (val ini = feed.initialState) {
                    InitialLoadState.Loading -> LoadingShimmerHome()
                    is InitialLoadState.Error -> ErrorView(
                        message = ini.error.toUiText(),
                        onRetry = onRetryInitial,
                        retryLabel = stringResource(R.string.retry),
                    )

                    InitialLoadState.Empty -> EmptyStateView(title = stringResource(R.string.home_empty))
                    InitialLoadState.Idle -> LoadingShimmerHome()
                    InitialLoadState.Ready -> {
                        if (visibleSections.isEmpty()) {
                            EmptyStateView(title = stringResource(R.string.home_empty_filter))
                        } else {
                            LazyColumn(state = listState, modifier = Modifier.padding(bottom = spaceMd)) {
                                itemsIndexed(
                                    visibleSections,
                                    key = { index, section -> "${section.id}:$index" },
                                ) { _, section ->
                                    val renderer = SectionRendererRegistry[section.layout]
                                    renderer(section, onContentClick = { onContentClick() })
                                }
                                item {
                                    AppendFooter(appendState = feed.appendState, onRetry = onAppendRetry)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun chipLabel(model: CategoryChipModel): String {
    if (model.id == HomeViewModel.FILTER_FOR_YOU) {
        return stringResource(R.string.chip_for_you)
    }
    return when (model.contentType) {
        ContentType.Podcast -> stringResource(R.string.chip_podcasts)
        ContentType.Episode -> stringResource(R.string.chip_episodes)
        ContentType.AudioBook -> stringResource(R.string.chip_books)
        ContentType.AudioArticle -> stringResource(R.string.chip_audio_articles)
        ContentType.Unknown, null -> stringResource(R.string.chip_other)
    }
}