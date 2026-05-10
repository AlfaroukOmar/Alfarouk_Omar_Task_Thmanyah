package com.thmanyah.feature.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.thmanyah.core.network.connectivity.ConnectivityObserver
import com.thmanyah.core.network.connectivity.ConnectivityStatus
import com.thmanyah.domain.entities.HomeSection
import com.thmanyah.domain.models.ContentType
import com.thmanyah.domain.repository.HomeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject


data class CategoryChipModel(
    val id: String,
    val contentType: ContentType?,
)

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val homeRepository: HomeRepository,
    connectivityObserver: ConnectivityObserver,
) : ViewModel() {

    val feed get() = homeRepository.state

    val isOffline: StateFlow<Boolean> = connectivityObserver.connectivity
        .map { it == ConnectivityStatus.Lost }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), false)

    private val _selectedFilterKey = MutableStateFlow(FILTER_FOR_YOU)
    val selectedFilterKey: StateFlow<String> = _selectedFilterKey

    val categoryChips: StateFlow<List<CategoryChipModel>> = homeRepository.state.map { feed ->
        val types = feed.sections
            .map { it.contentType }
            .distinct()
            .filter { it != ContentType.Unknown }
            .sortedBy { it.ordinal }
        listOf(CategoryChipModel(FILTER_FOR_YOU, null)) +
                types.map { CategoryChipModel(it.name, it) }
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5_000),
        listOf(CategoryChipModel(FILTER_FOR_YOU, null)),
    )

    val visibleSections: StateFlow<List<HomeSection>> = combine(
        homeRepository.state,
        _selectedFilterKey,
    ) { feed, key ->
        when {
            key == FILTER_FOR_YOU -> feed.sections
            else -> {
                val type = runCatching { ContentType.valueOf(key) }.getOrNull()
                if (type == null) {
                    feed.sections
                } else {
                    feed.sections.filter { it.contentType == type }
                }
            }
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    init {
        viewModelScope.launch {
            homeRepository.loadInitial()
        }
    }

    fun onAppendNearEnd() {
        viewModelScope.launch { homeRepository.loadNext() }
    }

    fun onRefresh() {
        viewModelScope.launch { homeRepository.refresh() }
    }

    fun onRetryInitial() {
        viewModelScope.launch { homeRepository.loadInitial() }
    }

    fun onAppendRetry() {
        viewModelScope.launch { homeRepository.loadNext() }
    }

    fun onFilterSelected(id: String) {
        _selectedFilterKey.value = id
    }

    fun onContentClick() {
        // Playback / detail not implemented
    }

    companion object {
        const val FILTER_FOR_YOU = "for_you"
    }
}
