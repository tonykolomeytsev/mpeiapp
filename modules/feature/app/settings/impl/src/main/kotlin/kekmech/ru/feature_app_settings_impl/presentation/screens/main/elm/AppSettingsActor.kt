package kekmech.ru.feature_app_settings_impl.presentation.screens.main.elm

import kekmech.ru.feature_app_settings_impl.data.AppSettingsRepositoryImpl
import kekmech.ru.feature_app_settings_impl.presentation.screens.main.elm.AppSettingsEvent.Internal
import kekmech.ru.feature_contributors_api.data.repository.ContributorsRepository
import kekmech.ru.lib_elm.actorFlow
import kotlinx.coroutines.flow.Flow
import money.vivid.elmslie.core.store.Actor

internal class AppSettingsActor(
    private val appSettingsRepository: AppSettingsRepositoryImpl,
    private val contributorsRepository: ContributorsRepository,
) : Actor<AppSettingsCommand, AppSettingsEvent>() {

    override fun execute(command: AppSettingsCommand): Flow<AppSettingsEvent> =
        when (command) {
            is AppSettingsCommand.LoadAppSettings -> actorFlow {
                appSettingsRepository.getAppSettings()
            }.mapEvents(Internal::LoadAppSettingsSuccess)

            is AppSettingsCommand.SetDarkThemeEnabled -> actorFlow {
                appSettingsRepository
                    .updateAppSettings { copy(isDarkThemeEnabled = command.isEnabled) }
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
                appSettingsRepository
                    .updateAppSettings { copy(languageCode = command.selectedLanguage) }
            }.mapEvents(Internal::LoadAppSettingsSuccess)

            is AppSettingsCommand.ChangeMapType -> actorFlow {
                appSettingsRepository
                    .updateAppSettings { copy(mapAppearanceType = command.selectedMapType) }
            }.mapEvents(Internal::LoadAppSettingsSuccess)

            is AppSettingsCommand.SetShowQuickNavigationFab -> actorFlow {
                appSettingsRepository
                    .updateAppSettings { copy(showNavigationButton = command.isVisible) }
            }.mapEvents(Internal::LoadAppSettingsSuccess)

            is AppSettingsCommand.FetchContributors ->
                actorFlow { contributorsRepository.fetchContributors() }.mapEvents()

            is AppSettingsCommand.ObserveContributors ->
                contributorsRepository.observeContributors()
                    .mapEvents(Internal::ObserveContributorsSuccess)
        }
}
