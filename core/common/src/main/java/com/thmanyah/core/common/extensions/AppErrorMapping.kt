package com.thmanyah.core.common.extensions

import com.thmanyah.core.common.R
import com.thmanyah.domain.models.AppError

fun AppError.toUiText(): UiText = when (this) {
    AppError.NoInternet -> UiText.Resource(R.string.error_no_internet)
    AppError.Timeout -> UiText.Resource(R.string.error_timeout)
    is AppError.Server -> UiText.Resource(R.string.error_server)
    AppError.Parsing -> UiText.Resource(R.string.error_unknown)
    is AppError.Unknown -> UiText.Resource(R.string.error_unknown)
}
