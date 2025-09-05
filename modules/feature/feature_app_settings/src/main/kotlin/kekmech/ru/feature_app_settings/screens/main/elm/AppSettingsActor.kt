package kekmech.ru.feature_app_settings.screens.main.elm

import kekmech.ru.common_elm.actorEmptyFlow
import kekmech.ru.common_elm.actorFlow
import kekmech.ru.domain_app_settings.AppSettingsRepository
import kekmech.ru.domain_github.ContributorsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.merge
import kotlinx.coroutines.rx3.asFlow
import kotlinx.coroutines.rx3.await
import money.vivid.elmslie.core.store.Actor

internal class AppSettingsActor(
    private val appSettingsRepository: AppSettingsRepository,
    private val contributorsRepository: ContributorsRepository,
) : Actor<AppSettingsCommand, AppSettingsEvent>() {

    override fun execute(command: AppSettingsCommand): Flow<AppSettingsEvent> =
        when (command) {
            is AppSettingsCommand.LoadAppSettings -> actorFlow {
                appSettingsRepository.getAppSettings().await()
            }.mapEvents(AppSettingsEvent.Internal::LoadAppSettingsSuccess)

            is AppSettingsCommand.SetDarkThemeEnabled -> actorFlow {
                appSettingsRepository
                    .changeAppSettings { copy(isDarkThemeEnabled = command.isEnabled) }
                    .await()
            }.mapEvents(AppSettingsEvent.Internal::LoadAppSettingsSuccess)

            is AppSettingsCommand.SetSnowEnabled -> actorFlow {
                appSettingsRepository
                    .changeAppSettings { copy(isSnowEnabled = command.isEnabled) }
                    .await()
            }.mapEvents(AppSettingsEvent.Internal::LoadAppSettingsSuccess)

            is AppSettingsCommand.SetAutoHideBottomSheet -> actorFlow {
                appSettingsRepository
                    .changeAppSettings { copy(autoHideBottomSheet = command.isEnabled) }
                    .await()
            }.mapEvents(AppSettingsEvent.Internal::LoadAppSettingsSuccess)

            is AppSettingsCommand.ChangeLanguage -> actorFlow {
                appSettingsRepository
                    .changeAppSettings { copy(languageCode = command.selectedLanguage) }
                    .await()
            }.mapEvents(AppSettingsEvent.Internal::LoadAppSettingsSuccess)

            is AppSettingsCommand.ChangeMapType -> actorFlow {
                appSettingsRepository
                    .changeAppSettings { copy(mapAppearanceType = command.selectedMapType) }
                    .await()
            }.mapEvents(AppSettingsEvent.Internal::LoadAppSettingsSuccess)

            is AppSettingsCommand.SetShowQuickNavigationFab -> actorFlow {
                appSettingsRepository
                    .changeAppSettings { copy(showNavigationButton = command.isVisible) }
                    .await()
            }.mapEvents(AppSettingsEvent.Internal::LoadAppSettingsSuccess)

            is AppSettingsCommand.ObserveContributors -> merge(
                contributorsRepository.observeContributors().asFlow(),
                flow { contributorsRepository.fetchContributors().await() },
            ).mapEvents(AppSettingsEvent.Internal::ObserveContributorsSuccess)

            is AppSettingsCommand.ClearSelectedGroup -> actorEmptyFlow { /* no-op */ }

            is AppSettingsCommand.ChangeBackendEnvironment -> actorFlow {
                appSettingsRepository
                    .changeAppSettings { copy(appEnvironment = command.appEnvironment) }
                    .await()
            }.mapEvents(AppSettingsEvent.Internal::LoadAppSettingsSuccess)
        }
}
