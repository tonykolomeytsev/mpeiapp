package kekmech.ru.debug_menu.presentation

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.core.view.WindowCompat
import com.bumble.appyx.core.integration.NodeHost
import com.bumble.appyx.core.integrationpoint.NodeComponentActivity
import kekmech.ru.debug_menu.presentation.navigation.BackStackRootNode
import kekmech.ru.feature_app_settings_api.data.AppSettingsRepository
import kekmech.ru.feature_app_settings_api.domain.AppTheme
import kekmech.ru.ui_theme.theme.MpeixTheme
import org.koin.android.ext.android.inject

internal class DebugMenuActivity : NodeComponentActivity() {

    private val appSettingsRepository: AppSettingsRepository by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        setContent {
            val appTheme by appSettingsRepository.observeAppTheme().collectAsState()
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
                    NodeHost(integrationPoint = appyxIntegrationPoint) {
                        BackStackRootNode(buildContext = it)
                    }
                }
            }
        }
    }
}
