package kekmech.ru.feature_bars_impl.presentation.screen.login

import androidx.compose.foundation.isSystemInDarkTheme
import kekmech.ru.feature_app_settings_api.domain.model.isDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import com.kekmech.lib_fragment.ComposeFragment
import kekmech.ru.feature_app_settings_api.data.AppSettingsRepository
import kekmech.ru.feature_bars_impl.presentation.screen.login.elm.BarsLoginStoreFactory
import kekmech.ru.lib_elm_compose.ElmContent
import kekmech.ru.ui_theme.theme.MpeixScreenTheme
import org.koin.compose.koinInject

internal class BarsLoginFragment : ComposeFragment() {

    @Composable
    override fun Screen() {
        val appSettings = koinInject<AppSettingsRepository>()
        val isSystemDark = isSystemInDarkTheme()
        val darkTheme =
            remember(isSystemDark) { derivedStateOf { appSettings.getAppSettings().isDarkTheme(isSystemDark) } }
        MpeixScreenTheme(darkTheme = darkTheme.value) {
            val factory = koinInject<BarsLoginStoreFactory>()
            ElmContent(
                key = "BarsLoginScreen",
                storeFactory = { factory.create() },
                content = ::BarsLoginScreen,
            )
        }
    }
}
