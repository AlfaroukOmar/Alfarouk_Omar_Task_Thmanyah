package com.thmanyah.alfarouk

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat
import com.thmanyah.core.common.di.SettingsRepository
import com.thmanyah.domain.prefs.AppLocale
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import timber.log.Timber
import javax.inject.Inject

@HiltAndroidApp
class ThmanyahApp : Application() {
    @Inject
    lateinit var settingsRepository: SettingsRepository

    override fun onCreate() {
        super.onCreate()
        runBlocking {
            val locale = settingsRepository.appLocale.first()
            val tags = when (locale) {
                AppLocale.Arabic -> "ar"
                AppLocale.English -> "en"
            }
            AppCompatDelegate.setApplicationLocales(LocaleListCompat.forLanguageTags(tags))
        }
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }
}