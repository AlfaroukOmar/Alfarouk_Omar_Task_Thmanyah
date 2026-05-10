package com.thmanyah.feature.setting

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.displayCutout
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle

import com.thmanyah.core.designsystem.theme.ThmanyahDimens
import com.thmanyah.core.designsystem.theme.ThmanyahDimens.spaceMd
import com.thmanyah.domain.prefs.AppLocale
import com.thmanyah.domain.prefs.DarkModeOption
import com.thmanyah.feature.setting.components.LanguageOptionRow
import com.thmanyah.feature.setting.components.SettingsSectionCard
import com.thmanyah.feature.setting.components.ThemeSegmentedSwitcher


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsRoute(
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: SettingsViewModel = hiltViewModel(),
) {
    val darkMode by viewModel.darkMode.collectAsStateWithLifecycle()
    val locale by viewModel.appLocale.collectAsStateWithLifecycle()
    val dedupeWhenMapping by viewModel.dedupeContentIdsWhenMapping.collectAsStateWithLifecycle()
    val repeatSectionsOnNextPage by viewModel.repeatHomeSectionsOnNextPage.collectAsStateWithLifecycle()
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()

    Scaffold(
        modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            LargeTopAppBar(
                modifier = Modifier.statusBarsPadding(),
                title = { Text(stringResource(R.string.settings_title)) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.settings_back_cd),
                        )
                    }
                },
                scrollBehavior = scrollBehavior,
            )
        },
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .windowInsetsPadding(WindowInsets.displayCutout.only(WindowInsetsSides.Horizontal))
                .verticalScroll(rememberScrollState())
                .padding(ThmanyahDimens.spaceMd),
            verticalArrangement = Arrangement.spacedBy(ThmanyahDimens.spaceLg),
        ) {
            SettingsSectionCard(
                title = stringResource(R.string.settings_theme_section),
                description = stringResource(R.string.settings_theme_description),
            ) {
                ThemeSegmentedSwitcher(
                    selected = darkMode,
                    onSelect = { viewModel.setDarkMode(it) },
                    labels = { option ->
                        when (option) {
                            DarkModeOption.System -> stringResource(R.string.settings_theme_system)
                            DarkModeOption.Light -> stringResource(R.string.settings_theme_light)
                            DarkModeOption.Dark -> stringResource(R.string.settings_theme_dark)
                        }
                    },
                )
            }

            SettingsSectionCard(
                title = stringResource(R.string.settings_language_section),
                description = stringResource(R.string.settings_language_description),
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(ThmanyahDimens.spaceSm)) {
                    LanguageOptionRow(
                        title = stringResource(R.string.settings_language_ar_title),
                        subtitle = stringResource(R.string.settings_language_ar_subtitle),
                        selected = locale == AppLocale.Arabic,
                        onClick = { viewModel.setLocale(AppLocale.Arabic) },
                    )
                    LanguageOptionRow(
                        title = stringResource(R.string.settings_language_en_title),
                        subtitle = stringResource(R.string.settings_language_en_subtitle),
                        selected = locale == AppLocale.English,
                        onClick = { viewModel.setLocale(AppLocale.English) },
                    )
                }
            }

            SettingsSectionCard(
                title = stringResource(R.string.settings_feed_pagination_section),
                description = stringResource(R.string.settings_feed_pagination_description),
            ) {
                SettingsToggleRow(
                    title = stringResource(R.string.settings_repeat_sections_title),
                    subtitle = stringResource(R.string.settings_repeat_sections_subtitle),
                    checked = repeatSectionsOnNextPage,
                    onCheckedChange = viewModel::setRepeatHomeSectionsOnNextPage,
                )
            }

            SettingsSectionCard(
                title = stringResource(R.string.settings_feed_api_mapping_section),
                description = stringResource(R.string.settings_feed_api_mapping_description),
            ) {
                SettingsToggleRow(
                    title = stringResource(R.string.settings_dedupe_api_title),
                    subtitle = stringResource(R.string.settings_dedupe_api_subtitle),
                    checked = dedupeWhenMapping,
                    onCheckedChange = viewModel::setDedupeContentIdsWhenMapping,
                )
            }

            SettingsSectionCard(
                title = stringResource(R.string.settings_about_section),
                description = null,
            ) {
                Text(
                    text = stringResource(R.string.settings_about_app_name),
                    style = MaterialTheme.typography.titleSmall,
                )
                Text(
                    text = stringResource(R.string.settings_about_body),
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(top = ThmanyahDimens.spaceSm),
                )
            }
        }
    }
}

@Composable
private fun SettingsToggleRow(
    title: String,
    subtitle: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(end = spaceMd),
        ) {
            Text(text = title, style = MaterialTheme.typography.titleSmall)
            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
        Switch(checked = checked, onCheckedChange = onCheckedChange)
    }
}
