package com.thmanyah.alfarouk

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.core.view.WindowCompat
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.thmanyah.core.common.di.SettingsRepository
import com.thmanyah.core.designsystem.theme.ThmanyahTheme
import com.thmanyah.domain.prefs.DarkModeOption
import com.thmanyah.feature.home.HomeRoute
import com.thmanyah.feature.search.SearchRoute
import com.thmanyah.feature.setting.SettingsRoute
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var settingsRepository: SettingsRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        WindowCompat.setDecorFitsSystemWindows(window, false)
        setContent {
            val darkMode by settingsRepository.darkMode.collectAsStateWithLifecycle(
                initialValue = DarkModeOption.System,
            )
            ThmanyahTheme(darkModePreference = darkMode) {
                val navController = rememberNavController()
                Surface(modifier = Modifier.fillMaxSize()) {
                    ThmanyahNavHost(navController = navController)
                }
            }
        }
    }
}

@Composable
private fun ThmanyahNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier,
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Home.route,
        modifier = modifier,
    ) {
        composable(Screen.Home.route) {
            HomeRoute(
                onOpenSearch = { navController.navigate(Screen.Search.route) },
                onOpenSettings = { navController.navigate(Screen.Settings.route) },
            )
        }
        composable(Screen.Search.route) {
            SearchRoute(onBack = { navController.popBackStack() })
        }
        composable(Screen.Settings.route) {
            SettingsRoute(onBack = { navController.popBackStack() })
        }
    }
}

sealed class Screen(val route: String) {
    data object Home : Screen("home")
    data object Search : Screen("search")
    data object Settings : Screen("settings")
}