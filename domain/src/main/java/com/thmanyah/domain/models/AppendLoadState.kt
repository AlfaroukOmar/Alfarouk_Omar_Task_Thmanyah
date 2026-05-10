package com.thmanyah.domain.models

sealed interface AppendLoadState {
    data object Idle : AppendLoadState
    data object Loading : AppendLoadState
    data class Error(val error: AppError) : AppendLoadState
    data object EndReached : AppendLoadState
}