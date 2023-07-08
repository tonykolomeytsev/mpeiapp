package kekmech.ru.mpeiapp.demo

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.core.view.WindowCompat
import com.bumble.appyx.core.integration.NodeHost
import com.bumble.appyx.core.integrationpoint.NodeComponentActivity
import kekmech.ru.lib_navigation_compose.node.backstack.BackStackNode
import kekmech.ru.mpeiapp.demo.di.AppModule
import kekmech.ru.mpeiapp.demo.screens.main.MainScreenNavTarget
import kekmech.ru.mpeiapp.demo.ui.LocalUiKitThemeController
import kekmech.ru.mpeiapp.demo.ui.UiKitThemeController
import kekmech.ru.ui_theme.theme.MpeixTheme
import org.koin.core.context.startKoin

class MainActivity : NodeComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        startKoin {
            modules(AppModule)
        }
        WindowCompat.setDecorFitsSystemWindows(window, false)
        setContent {
            val isSystemInDarkTheme = isSystemInDarkTheme()
            var darkThemeEnabled by remember { mutableStateOf(isSystemInDarkTheme) }
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
                        color = MpeixTheme.palette.background,
                    ) {
                        NodeHost(integrationPoint = appyxIntegrationPoint) { buildContext ->
                            BackStackNode(
                                buildContext = buildContext,
                                rootNavTarget = MainScreenNavTarget(),
                            )
                        }
                    }
                }
            }
        }
    }
}
