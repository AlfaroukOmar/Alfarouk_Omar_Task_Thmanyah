package com.thmanyah.data.repository.home

import com.thmanyah.core.common.dispatcher.IoDispatcher
import com.thmanyah.core.network.connectivity.ConnectivityObserver
import com.thmanyah.core.network.connectivity.ConnectivityStatus
import com.thmanyah.core.network.error.ErrorMapper
import com.thmanyah.data.remote.api.HomeApi
import com.thmanyah.data.remote.mapper.SectionMapper
import com.thmanyah.domain.models.AppResult
import com.thmanyah.domain.models.AppendLoadState
import com.thmanyah.domain.models.HomeFeedState
import com.thmanyah.domain.models.InitialLoadState
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class HomePaginator @Inject constructor(
    private val api: HomeApi,
    private val mapper: SectionMapper,
    @IoDispatcher private val io: CoroutineDispatcher,
    connectivityObserver: ConnectivityObserver,
) {
    private val scope = CoroutineScope(SupervisorJob() + io)
    private val mutex = Mutex()

    private val _state = MutableStateFlow(HomeFeedState())
    val state: StateFlow<HomeFeedState> = _state.asStateFlow()

    init {
        scope.launch {
            var wasOffline = false
            connectivityObserver.connectivity.collect { status ->
                if (status == ConnectivityStatus.Lost) {
                    wasOffline = true
                } else if (wasOffline && status == ConnectivityStatus.Available) {
                    wasOffline = false
                    val append = _state.value.appendState
                    if (append is AppendLoadState.Error) {
                        loadNext()
                    }
                }
            }
        }
    }

    suspend fun restore(snapshot: HomeFeedState) = mutex.withLock {
        _state.value = snapshot
    }

    suspend fun loadInitial() = mutex.withLock {
        _state.value = _state.value.copy(initialState = InitialLoadState.Loading)
        val result = withContext(io) {
            runCatching { api.getHome(1) }.fold(
                onSuccess = { AppResult.Success(it) },
                onFailure = { AppResult.Failure(ErrorMapper.map(it)) },
            )
        }
        when (result) {
            is AppResult.Success -> {
                val dedupeWhenMapping =true //TODO get from setting later
                val sections = mapper.mapPage(result.data, dedupeWhenMapping)
                val totalPages = result.data.pagination?.total_pages?.takeIf { it > 0 } ?: 1
                _state.value = HomeFeedState(
                    sections = sections,
                    initialState = if (sections.isEmpty()) InitialLoadState.Empty else InitialLoadState.Ready,
                    appendState = AppendLoadState.Idle,
                    page = 1,
                    totalPages = totalPages,
                    refreshGeneration = _state.value.refreshGeneration,
                )
            }

            is AppResult.Failure -> {
                _state.value = _state.value.copy(
                    initialState = InitialLoadState.Error(result.error),
                    sections = emptyList(),
                )
            }
        }
    }

    suspend fun loadNext() = mutex.withLock {
        val current = _state.value
        if (current.initialState != InitialLoadState.Ready) return@withLock
        if (current.appendState is AppendLoadState.Loading) return@withLock
        if (current.appendState is AppendLoadState.EndReached) return@withLock
        if (current.page >= current.totalPages) {
            _state.value = current.copy(appendState = AppendLoadState.EndReached)
            return@withLock
        }
        val nextPage = current.page + 1
        _state.value = current.copy(appendState = AppendLoadState.Loading)

        val result = withContext(io) {
            runCatching { api.getHome(nextPage) }.fold(
                onSuccess = { AppResult.Success(it) },
                onFailure = { AppResult.Failure(ErrorMapper.map(it)) },
            )
        }

        when (result) {
            is AppResult.Success -> {
                val dedupeWhenMapping = true //TODO get from setting later
                val repeatHomeSectionsOnNextPage = true //TODO get from setting later
                val incoming = mapper.mapPage(result.data, dedupeWhenMapping)
                val combined = if (repeatHomeSectionsOnNextPage) {
                    HomeFeedMerger.appendPagedSections(current.sections, incoming, nextPage)
                } else {
                    HomeFeedMerger.mergeSections(current.sections, incoming, dedupeMergedItems = dedupeWhenMapping)
                }
                val endReached = nextPage >= current.totalPages
                _state.value = current.copy(
                    sections = combined,
                    appendState = if (endReached) AppendLoadState.EndReached else AppendLoadState.Idle,
                    page = nextPage,
                )
            }

            is AppResult.Failure -> {
                _state.value = current.copy(appendState = AppendLoadState.Error(result.error))
            }
        }
    }

    suspend fun refresh() = mutex.withLock {
        val snapshot = _state.value
        _state.value = snapshot.copy(
            refreshGeneration = snapshot.refreshGeneration + 1,
            initialState = InitialLoadState.Loading,
            appendState = AppendLoadState.Idle,
            page = 0,
        )
        val result = withContext(io) {
            runCatching { api.getHome(1) }.fold(
                onSuccess = { AppResult.Success(it) },
                onFailure = { AppResult.Failure(ErrorMapper.map(it)) },
            )
        }
        when (result) {
            is AppResult.Success -> {
                val dedupeWhenMapping = true //TODO get from setting later
                val sections = mapper.mapPage(result.data, dedupeWhenMapping)
                val totalPages = result.data.pagination?.total_pages?.takeIf { it > 0 } ?: 1
                _state.value = HomeFeedState(
                    sections = sections,
                    initialState = if (sections.isEmpty()) InitialLoadState.Empty else InitialLoadState.Ready,
                    appendState = AppendLoadState.Idle,
                    page = 1,
                    totalPages = totalPages,
                    refreshGeneration = _state.value.refreshGeneration,
                )
            }

            is AppResult.Failure -> {
                _state.value = snapshot.copy(
                    initialState = InitialLoadState.Error(result.error),
                )
            }
        }
    }

}
