package com.thmanyah.feature.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.thmanyah.core.common.extensions.UiText
import com.thmanyah.core.common.extensions.toUiText
import com.thmanyah.domain.entities.HomeSection
import com.thmanyah.domain.models.AppResult
import com.thmanyah.domain.repository.SearchRepository

import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import javax.inject.Inject

sealed interface SearchUiState {
    data object Idle : SearchUiState
    data class Loading(val previous: List<HomeSection>) : SearchUiState
    data class Success(val results: List<HomeSection>) : SearchUiState
    data class Empty(val query: String) : SearchUiState
    data class Error(
        val message: UiText,
        val previous: List<HomeSection>,
    ) : SearchUiState
}

@OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
@HiltViewModel
class SearchViewModel @Inject constructor(
    private val searchRepository: SearchRepository,
) : ViewModel() {

    private val query = MutableStateFlow("")
    /** Bumped on retry so [distinctUntilChanged] does not drop a repeat of the same query. */
    private val retryEpoch = MutableStateFlow(0)
    val queryForUi = query.asStateFlow()

    private var lastSuccess: List<HomeSection> = emptyList()

    val state = combine(query, retryEpoch) { q, epoch -> q to epoch }
        .debounce(200)
        .distinctUntilChanged()
        .map { pair -> pair.first }
        .flatMapLatest { q ->
            if (q.isBlank()) {
                flowOf<SearchUiState>(SearchUiState.Idle)
            } else {
                flow<SearchUiState> {
                    emit(SearchUiState.Loading(lastSuccess))
                    searchRepository.search(q).collect { res ->
                        when (res) {
                            is AppResult.Success -> {
                                lastSuccess = res.data
                                emit(
                                    if (res.data.isEmpty()) {
                                        SearchUiState.Empty(q)
                                    } else {
                                        SearchUiState.Success(res.data)
                                    },
                                )
                            }

                            is AppResult.Failure -> {
                                emit(
                                    SearchUiState.Error(
                                        message = res.error.toUiText(),
                                        previous = lastSuccess,
                                    ),
                                )
                            }
                        }
                    }
                }
            }
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), SearchUiState.Idle)

    fun onQueryChange(raw: String) {
        query.value = raw
    }

    fun clearQuery() {
        query.value = ""
    }

    fun retry() {
        retryEpoch.update { it + 1 }
    }
}
