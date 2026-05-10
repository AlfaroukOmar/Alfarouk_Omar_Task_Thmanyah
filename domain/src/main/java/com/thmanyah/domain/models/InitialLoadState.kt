package com.thmanyah.domain.models

sealed interface InitialLoadState {
    data object Idle : InitialLoadState
    data object Loading : InitialLoadState
    data object Ready : InitialLoadState
    data object Empty : InitialLoadState
    data class Error(val error: AppError) : InitialLoadState
}