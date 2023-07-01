package kekmech.ru.feature_app_settings.screens.main.elm

import kekmech.ru.common_elm.actorFlow
import kekmech.ru.domain_app_settings.AppSettingsRepository
import kekmech.ru.domain_github.ContributorsRepository
import kekmech.ru.feature_app_settings.screens.main.elm.AppSettingsEvent.Internal
import kotlinx.coroutines.flow.Flow
import vivid.money.elmslie.coroutines.Actor

internal class AppSettingsActor(
    private val appSettingsRepository: AppSettingsRepository,
    private val contributorsRepository: ContributorsRepository,
) : Actor<AppSettingsCommand, AppSettingsEvent> {

    override fun execute(command: AppSettingsCommand): Flow<AppSettingsEvent> =
        when (command) {
            is AppSettingsCommand.LoadAppSettings -> actorFlow {
                appSettingsRepository.getAppSettings()
            }.mapEvents(Internal::LoadAppSettingsSuccess)

            is AppSettingsCommand.SetDarkThemeEnabled -> actorFlow {
                appSettingsRepository
                    .changeAppSettings { copy(isDarkThemeEnabled = command.isEnabled) }
            }.mapEvents(Internal::LoadAppSettingsSuccess)

            is AppSettingsCommand.SetSnowEnabled -> actorFlow {
                appSettingsRepository
                    .changeAppSettings { copy(isSnowEnabled = command.isEnabled) }
            }.mapEvents(Internal::LoadAppSettingsSuccess)

            is AppSettingsCommand.SetAutoHideBottomSheet -> actorFlow {
                appSettingsRepository
                    .changeAppSettings { copy(autoHideBottomSheet = command.isEnabled) }
            }.mapEvents(Internal::LoadAppSettingsSuccess)

            is AppSettingsCommand.ChangeLanguage -> actorFlow {
                appSettingsRepository
                    .changeAppSettings { copy(languageCode = command.selectedLanguage) }
            }.mapEvents(Internal::LoadAppSettingsSuccess)

            is AppSettingsCommand.ChangeMapType -> actorFlow {
                appSettingsRepository
                    .changeAppSettings { copy(mapAppearanceType = command.selectedMapType) }
            }.mapEvents(Internal::LoadAppSettingsSuccess)

            is AppSettingsCommand.SetShowQuickNavigationFab -> actorFlow {
                appSettingsRepository
                    .changeAppSettings { copy(showNavigationButton = command.isVisible) }
            }.mapEvents(Internal::LoadAppSettingsSuccess)

            is AppSettingsCommand.FetchContributors ->
                actorFlow { contributorsRepository.fetchContributors() }.mapEvents()

            is AppSettingsCommand.ObserveContributors ->
                contributorsRepository.observeContributors()
                    .mapEvents(Internal::ObserveContributorsSuccess)
        }
}
