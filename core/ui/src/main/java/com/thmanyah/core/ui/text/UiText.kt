package com.thmanyah.core.ui.text


import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import com.thmanyah.core.common.extensions.UiText


@Composable
fun UiText.resolveString(): String = when (this) {
    is UiText.Dynamic -> value
    is UiText.Resource -> {
        val ctx = LocalContext.current
        if (args.isEmpty()) stringResource(resId) else stringResource(resId, *args.toTypedArray())
    }
}

