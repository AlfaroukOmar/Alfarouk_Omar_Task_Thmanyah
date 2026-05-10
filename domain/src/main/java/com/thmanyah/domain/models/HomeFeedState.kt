package com.thmanyah.domain.models

import com.thmanyah.domain.entities.HomeSection

data class HomeFeedState(
    val sections: List<HomeSection> = emptyList(),
    val initialState: InitialLoadState = InitialLoadState.Idle,
    val appendState: AppendLoadState = AppendLoadState.Idle,
    /** Last successfully loaded page number (1-based). */
    val page: Int = 0,
    val totalPages: Int = 1,
    val refreshGeneration: Long = 0L,
)