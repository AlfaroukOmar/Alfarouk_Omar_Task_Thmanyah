package com.thmanyah.core.common.extensions

import androidx.annotation.StringRes

sealed interface UiText {
    data class Dynamic(val value: String) : UiText
    data class Resource(@StringRes val resId: Int, val args: List<Any> = emptyList()) : UiText
}

fun UiText.resolve(getString: (Int, Array<Any>) -> String): String = when (this) {
    is UiText.Dynamic -> value
    is UiText.Resource -> getString(resId, args.toTypedArray())
}
