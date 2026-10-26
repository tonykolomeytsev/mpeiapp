package kekmech.ru.feature_app_settings_impl.presentation.screens.main.elm

import kekmech.ru.ext_android.AppLocales
import kekmech.ru.feature_app_settings_api.domain.model.AppTheme
import kekmech.ru.feature_app_settings_impl.data.AppSettingsRepositoryImpl
import kekmech.ru.feature_app_settings_impl.presentation.screens.main.elm.AppSettingsEvent.Internal
import kekmech.ru.lib_elm.actorFlow
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import money.vivid.elmslie.core.store.Actor

internal class AppSettingsActor(
    private val appSettingsRepository: AppSettingsRepositoryImpl,
) : Actor<AppSettingsCommand, AppSettingsEvent>() {

    override fun execute(command: AppSettingsCommand): Flow<AppSettingsEvent> =
        when (command) {
            is AppSettingsCommand.LoadAppSettings -> actorFlow {
                appSettingsRepository.getAppSettings()
            }.mapEvents(Internal::LoadAppSettingsSuccess)

            is AppSettingsCommand.SetDarkThemeEnabled -> actorFlow {
                appSettingsRepository
                    .updateAppSettings {
                        copy(appTheme = if (command.isEnabled) AppTheme.Dark else AppTheme.Light)
                    }
            }.mapEvents(Internal::LoadAppSettingsSuccess)

            is AppSettingsCommand.SetTheme -> actorFlow {
                appSettingsRepository
                    .updateAppSettings { copy(appTheme = command.theme) }
            }.mapEvents(Internal::LoadAppSettingsSuccess)

            is AppSettingsCommand.SetSnowEnabled -> actorFlow {
                appSettingsRepository
                    .updateAppSettings { copy(isSnowEnabled = command.isEnabled) }
            }.mapEvents(Internal::LoadAppSettingsSuccess)

            is AppSettingsCommand.SetAutoHideBottomSheet -> actorFlow {
                appSettingsRepository
                    .updateAppSettings { copy(autoHideBottomSheet = command.isEnabled) }
            }.mapEvents(Internal::LoadAppSettingsSuccess)

            is AppSettingsCommand.ChangeLanguage -> actorFlow {
                val settings = appSettingsRepository
                    .updateAppSettings { copy(languageCode = command.selectedLanguage) }
                // The language is saved first: applying it recreates the activity
                withContext(Dispatchers.Main) { AppLocales.apply(command.selectedLanguage) }
                settings
            }.mapEvents(Internal::LoadAppSettingsSuccess)

            is AppSettingsCommand.ChangeMapType -> actorFlow {
                appSettingsRepository
                    .updateAppSettings { copy(mapAppearanceType = command.selectedMapType) }
            }.mapEvents(Internal::LoadAppSettingsSuccess)

            is AppSettingsCommand.SetShowQuickNavigationFab -> actorFlow {
                appSettingsRepository
                    .updateAppSettings { copy(showNavigationButton = command.isVisible) }
            }.mapEvents(Internal::LoadAppSettingsSuccess)
        }
}
