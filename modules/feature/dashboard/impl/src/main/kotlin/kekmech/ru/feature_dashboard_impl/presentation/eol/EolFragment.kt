package kekmech.ru.feature_dashboard_impl.presentation.eol

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import com.kekmech.lib_fragment.ComposeFragment
import kekmech.ru.feature_app_settings_api.data.AppSettingsRepository
import kekmech.ru.feature_app_settings_api.domain.model.isDarkTheme
import kekmech.ru.lib_navigation.PopBackStack
import kekmech.ru.lib_navigation.Router
import kekmech.ru.ui_theme.theme.MpeixScreenTheme
import org.koin.android.ext.android.inject
import org.koin.compose.koinInject

internal class EolFragment : ComposeFragment() {

    private val router: Router by inject()

    @Composable
    override fun Screen() {
        val appSettings = koinInject<AppSettingsRepository>()
        val isSystemDark = isSystemInDarkTheme()
        val darkTheme = remember(isSystemDark) { appSettings.getAppSettings().isDarkTheme(isSystemDark) }
        MpeixScreenTheme(darkTheme = darkTheme) {
            EolScreen(
                onBackClick = { router.executeCommand(PopBackStack()) }
            )
        }
    }
}
