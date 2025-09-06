package kekmech.ru.debug_menu.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.core.view.WindowCompat
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.runtime.rememberSavedStateNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import androidx.navigation3.ui.rememberSceneSetupNavEntryDecorator
import kekmech.ru.debug_menu.presentation.navigation.LocalNavBackStack
import kekmech.ru.debug_menu.presentation.navigation.NavScreen
import kekmech.ru.debug_menu.presentation.navigation.WithBackStack
import kekmech.ru.debug_menu.presentation.screens.main.DebugMenuScreen
import kekmech.ru.feature_app_settings_api.domain.model.AppTheme
import kekmech.ru.feature_app_settings_api.domain.usecase.ObserveAppThemeUseCase
import kekmech.ru.ui_theme.theme.MpeixTheme
import org.koin.android.ext.android.inject

internal class DebugMenuActivity : ComponentActivity() {

    private val observeAppThemeUseCase: ObserveAppThemeUseCase by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        setContent {
            val appTheme by observeAppThemeUseCase.invoke().collectAsState()
            val darkTheme = when (appTheme) {
                AppTheme.Light -> false
                AppTheme.Dark -> true
                AppTheme.System -> isSystemInDarkTheme()
            }

            MpeixTheme(darkTheme = darkTheme) {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MpeixTheme.palette.background
                ) {
                    val backStack = rememberNavBackStack<NavScreen>(DebugMenuScreen)
                    WithBackStack(backStack) {
                        Content()
                    }
                }
            }
        }
    }

    @Composable
    private fun Content() {
        NavDisplay(
            backStack = LocalNavBackStack.current,
            modifier = Modifier.fillMaxSize(),
            entryDecorators = listOf(
                rememberSavedStateNavEntryDecorator(),
                rememberSceneSetupNavEntryDecorator(),
            ),
        ) { key ->
            when (key) {
                is NavScreen -> NavEntry(key) { key.Content() }
                else -> error("Invalid nav key!")
            }
        }
    }
}
