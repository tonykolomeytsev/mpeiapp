package kekmech.ru.feature_app_settings_impl.launcher

import kekmech.ru.feature_app_settings_api.AppSettingsFeatureLauncher
import kekmech.ru.feature_app_settings_api.AppSettingsFeatureLauncher.SubPage.FAVORITES
import kekmech.ru.feature_app_settings_impl.presentation.screens.favorites.FavoritesFragment
import kekmech.ru.feature_app_settings_impl.presentation.screens.main.AppSettingsFragment
import kekmech.ru.lib_navigation.AddScreenForward
import kekmech.ru.lib_navigation.Router

internal class AppSettingsFeatureLauncherImpl(
    private val router: Router,
) : AppSettingsFeatureLauncher {

    override fun launch(subPage: AppSettingsFeatureLauncher.SubPage?) {
        when (subPage) {
            FAVORITES -> router.executeCommand(AddScreenForward { FavoritesFragment() })
            else -> router.executeCommand(AddScreenForward { AppSettingsFragment() })
        }
    }
}
