package com.thmanyah.core.ui.preview

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.LayoutDirection
import com.thmanyah.core.designsystem.theme.ThmanyahTheme
import com.thmanyah.domain.prefs.DarkModeOption

@Composable
fun ThmanyahPreviewSurface(
    darkTheme: Boolean,
    layoutDirection: LayoutDirection = LayoutDirection.Ltr,
    content: @Composable () -> Unit,
) {
    androidx.compose.runtime.CompositionLocalProvider(LocalLayoutDirection provides layoutDirection) {
       ThmanyahTheme(darkModePreference = if (darkTheme) DarkModeOption.Dark else DarkModeOption.Light) {
            Surface(modifier = Modifier.fillMaxSize()) {
                content()
            }
        }
    }
}

class DarkThemeParameterProvider : PreviewParameterProvider<Boolean> {
    override val values: Sequence<Boolean> = sequenceOf(false, true)
}

class LayoutDirectionParameterProvider : PreviewParameterProvider<LayoutDirection> {
    override val values: Sequence<LayoutDirection> = sequenceOf(LayoutDirection.Ltr, LayoutDirection.Rtl)
}
