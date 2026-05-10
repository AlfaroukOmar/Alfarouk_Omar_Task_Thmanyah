package com.thmanyah.core.common.result

import com.thmanyah.core.common.extensions.UiText

sealed interface UiState<out T> {
    data object Loading : UiState<Nothing>
    data class Success<T>(val data: T) : UiState<T>
    data class Error(val message: UiText, val isRetryable: Boolean = true) : UiState<Nothing>
    data object Empty : UiState<Nothing>
}
