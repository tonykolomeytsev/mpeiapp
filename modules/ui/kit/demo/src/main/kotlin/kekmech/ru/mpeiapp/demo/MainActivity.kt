package kekmech.ru.mpeiapp.demo

import android.graphics.Color
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.ui.NavDisplay
import kekmech.ru.mpeiapp.demo.di.AppModule
import kekmech.ru.mpeiapp.demo.navigation.LocalNavBackStack
import kekmech.ru.mpeiapp.demo.navigation.NavScreen
import kekmech.ru.mpeiapp.demo.navigation.WithBackStack
import kekmech.ru.mpeiapp.demo.screens.main.MainScreen
import kekmech.ru.mpeiapp.demo.ui.LocalUiKitThemeController
import kekmech.ru.mpeiapp.demo.ui.UiKitThemeController
import kekmech.ru.ui_theme.theme.MpeixTheme
import org.koin.core.context.GlobalContext
import org.koin.core.context.startKoin

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        GlobalContext.getOrNull() ?: startKoin {
            modules(AppModule)
        }
        setContent {
            val isSystemInDarkTheme = isSystemInDarkTheme()
            var darkThemeEnabled by remember { mutableStateOf(isSystemInDarkTheme) }
            // The theme is toggled without recreating the activity, so the bars follow it here
            DisposableEffect(darkThemeEnabled) {
                val style = if (darkThemeEnabled) {
                    SystemBarStyle.dark(Color.TRANSPARENT)
                } else {
                    SystemBarStyle.light(Color.TRANSPARENT, Color.TRANSPARENT)
                }
                enableEdgeToEdge(statusBarStyle = style, navigationBarStyle = style)
                onDispose {}
            }
            val themeController = remember {
                object : UiKitThemeController {

                    override fun darkThemeEnabled(): Boolean =
                        darkThemeEnabled

                    override fun toggleDarkTheme() {
                        darkThemeEnabled = !darkThemeEnabled
                    }
                }
            }
            CompositionLocalProvider(
                LocalUiKitThemeController provides themeController,
            ) {
                MpeixTheme(
                    darkTheme = darkThemeEnabled,
                ) {
                    // A surface container using the 'background' color from the theme
                    Surface(
                        modifier = Modifier.fillMaxSize(),
                        color = MpeixTheme.palette.surface,
                    ) {
                        val backStack = rememberNavBackStack(MainScreen)
                        WithBackStack(backStack) {
                            NavDisplay(
                                backStack = LocalNavBackStack.current,
                                modifier = Modifier.fillMaxSize(),
                            ) { key ->
                                when (key) {
                                    is NavScreen -> NavEntry<NavKey>(key) { key.Content() }
                                    else -> error("Invalid nav key!")
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
