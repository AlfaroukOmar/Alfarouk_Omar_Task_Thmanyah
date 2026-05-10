package com.thmanyah.feature.setting

import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.thmanyah.core.common.di.SettingsRepository
import com.thmanyah.domain.prefs.AppLocale
import com.thmanyah.domain.prefs.DarkModeOption
import com.thmanyah.domain.repository.HomeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val settingsRepository: SettingsRepository,
    private val homeRepository: HomeRepository,
) : ViewModel() {

    val darkMode: StateFlow<DarkModeOption> = settingsRepository.darkMode
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), DarkModeOption.Light)

    val appLocale: StateFlow<AppLocale> = settingsRepository.appLocale
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), AppLocale.English)

    val dedupeContentIdsWhenMapping: StateFlow<Boolean> = settingsRepository.dedupeContentIdsWhenMapping
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), true)

    val repeatHomeSectionsOnNextPage: StateFlow<Boolean> = settingsRepository.repeatHomeSectionsOnNextPage
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), true)

    fun setDarkMode(option: DarkModeOption) {
        viewModelScope.launch {
            settingsRepository.setDarkMode(option)
        }
    }

    fun setLocale(locale: AppLocale) {
        viewModelScope.launch {
            settingsRepository.setAppLocale(locale)
            val tags = when (locale) {
                AppLocale.Arabic -> "ar"
                AppLocale.English -> "en"
            }
            AppCompatDelegate.setApplicationLocales(LocaleListCompat.forLanguageTags(tags))
        }
    }

    fun setDedupeContentIdsWhenMapping(enabled: Boolean) {
        viewModelScope.launch {
            settingsRepository.setDedupeContentIdsWhenMapping(enabled)
            homeRepository.refresh()
        }
    }

    fun setRepeatHomeSectionsOnNextPage(enabled: Boolean) {
        viewModelScope.launch {
            settingsRepository.setRepeatHomeSectionsOnNextPage(enabled)
            homeRepository.refresh()
        }
    }
}