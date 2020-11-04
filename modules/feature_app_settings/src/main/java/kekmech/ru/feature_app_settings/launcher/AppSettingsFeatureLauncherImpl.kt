package kekmech.ru.feature_app_settings.launcher

import kekmech.ru.common_navigation.AddScreenForward
import kekmech.ru.common_navigation.Router
import kekmech.ru.domain_app_settings.AppSettingsFeatureLauncher
import kekmech.ru.feature_app_settings.screens.main.AppSettingsFragment

internal class AppSettingsFeatureLauncherImpl(
    private val router: Router
) : AppSettingsFeatureLauncher {

    override fun launch() {
        router.executeCommand(AddScreenForward { AppSettingsFragment() })
    }
}