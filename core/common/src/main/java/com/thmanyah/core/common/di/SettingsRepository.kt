package com.thmanyah.core.common.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.thmanyah.domain.prefs.AppLocale
import com.thmanyah.domain.prefs.DarkModeOption
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

private val Context.thmanyahSettingsDataStore: DataStore<Preferences> by preferencesDataStore(name = "thmanyah_settings")

interface SettingsRepository {
    val darkMode: Flow<DarkModeOption>
    val appLocale: Flow<AppLocale>
    /** When true (default), drop duplicate content IDs when mapping each API section. */
    val dedupeContentIdsWhenMapping: Flow<Boolean>
    /**
     * When true (default), loading the next home page appends a new copy of each section block.
     * When false, items from the next page are merged into matching sections from the first page.
     */
    val repeatHomeSectionsOnNextPage: Flow<Boolean>
    suspend fun setDarkMode(option: DarkModeOption)
    suspend fun setAppLocale(locale: AppLocale)
    suspend fun setDedupeContentIdsWhenMapping(enabled: Boolean)
    suspend fun setRepeatHomeSectionsOnNextPage(enabled: Boolean)
}

@Singleton
class SettingsRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context,
) : SettingsRepository {

    private val dataStore = context.thmanyahSettingsDataStore

    private val keyDark = stringPreferencesKey("dark_mode")
    private val keyLocale = stringPreferencesKey("app_locale")
    private val keyDedupeContentIdsMapping = booleanPreferencesKey("dedupe_content_ids_when_mapping")
    private val keyRepeatHomeSectionsOnNextPage = booleanPreferencesKey("repeat_home_sections_on_next_page")

    override val darkMode: Flow<DarkModeOption> = dataStore.data.map { prefs ->
        when (prefs[keyDark]) {
            "light" -> DarkModeOption.Light
            "dark" -> DarkModeOption.Dark
            else -> DarkModeOption.System
        }
    }

    override val appLocale: Flow<AppLocale> = dataStore.data.map { prefs ->
        when (prefs[keyLocale]) {
            "en" -> AppLocale.English
            else -> AppLocale.Arabic
        }
    }

    override val dedupeContentIdsWhenMapping: Flow<Boolean> = dataStore.data.map { prefs ->
        prefs[keyDedupeContentIdsMapping] ?: true
    }

    override val repeatHomeSectionsOnNextPage: Flow<Boolean> = dataStore.data.map { prefs ->
        prefs[keyRepeatHomeSectionsOnNextPage] ?: true
    }

    override suspend fun setDarkMode(option: DarkModeOption) {
        dataStore.edit { prefs ->
            prefs[keyDark] = when (option) {
                DarkModeOption.System -> "system"
                DarkModeOption.Light -> "light"
                DarkModeOption.Dark -> "dark"
            }
        }
    }

    override suspend fun setAppLocale(locale: AppLocale) {
        dataStore.edit { prefs ->
            prefs[keyLocale] = when (locale) {
                AppLocale.Arabic -> "ar"
                AppLocale.English -> "en"
            }
        }
    }

    override suspend fun setDedupeContentIdsWhenMapping(enabled: Boolean) {
        dataStore.edit { prefs ->
            prefs[keyDedupeContentIdsMapping] = enabled
        }
    }

    override suspend fun setRepeatHomeSectionsOnNextPage(enabled: Boolean) {
        dataStore.edit { prefs ->
            prefs[keyRepeatHomeSectionsOnNextPage] = enabled
        }
    }
}
