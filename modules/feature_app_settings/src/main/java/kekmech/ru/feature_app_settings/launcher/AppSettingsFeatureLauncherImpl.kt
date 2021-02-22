package kekmech.ru.feature_app_settings.launcher

import kekmech.ru.common_navigation.AddScreenForward
import kekmech.ru.common_navigation.Router
import kekmech.ru.domain_app_settings.AppSettingsFeatureLauncher
import kekmech.ru.domain_app_settings.AppSettingsFeatureLauncher.SubPage.FAVORITES
import kekmech.ru.feature_app_settings.screens.favorites.FavoritesFragment
import kekmech.ru.feature_app_settings.screens.main.AppSettingsFragment

internal class AppSettingsFeatureLauncherImpl(
    private val router: Router
) : AppSettingsFeatureLauncher {

    override fun launch(subPage: AppSettingsFeatureLauncher.SubPage?) {
        when (subPage) {
            FAVORITES -> router.executeCommand(AddScreenForward { FavoritesFragment() })
            else -> router.executeCommand(AddScreenForward { AppSettingsFragment() })
        }
    }
}