package kekmech.ru.feature_app_settings_impl.launcher

import kekmech.ru.feature_app_settings_api.AppSettingsFeatureLauncher
import kekmech.ru.feature_app_settings_api.AppSettingsFeatureLauncher.SubPage.FAVORITES
import kekmech.ru.feature_app_settings_api.data.AppSettingsRepository
import kekmech.ru.feature_app_settings_impl.navigation.AppSettingsScreenKey
import kekmech.ru.feature_app_settings_impl.navigation.FavoritesScreenKey
import kekmech.ru.feature_app_settings_impl.presentation.screens.lang.SelectLanguageFragment
import kekmech.ru.lib_navigation.AddScreenForward
import kekmech.ru.lib_navigation.Router
import kekmech.ru.lib_navigation.ShowDialog

internal class AppSettingsFeatureLauncherImpl(
    private val router: Router,
    private val appSettingsRepository: AppSettingsRepository,
) : AppSettingsFeatureLauncher {

    override fun launch(subPage: AppSettingsFeatureLauncher.SubPage?) {
        when (subPage) {
            FAVORITES -> router.executeCommand(AddScreenForward(FavoritesScreenKey))
            else -> router.executeCommand(AddScreenForward(AppSettingsScreenKey))
        }
    }

    override fun launchLanguageSelection(resultKey: String) {
        val selectedLanguage = appSettingsRepository.getAppSettings().languageCode
        router.executeCommand(ShowDialog {
            SelectLanguageFragment.newInstance(selectedLanguage, resultKey)
        })
    }
}
