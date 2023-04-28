package kekmech.ru.feature_app_settings.screens.main.elm

import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Observable
import kekmech.ru.domain_app_settings.AppSettingsRepository
import kekmech.ru.domain_github.ContributorsRepository
import kekmech.ru.domain_schedule.repository.ScheduleRepository
import vivid.money.elmslie.core.store.Actor

internal class AppSettingsActor(
    private val appSettingsRepository: AppSettingsRepository,
    private val scheduleRepository: ScheduleRepository,
    private val contributorsRepository: ContributorsRepository,
) : Actor<AppSettingsCommand, AppSettingsEvent> {

    override fun execute(command: AppSettingsCommand): Observable<AppSettingsEvent> =
        when (command) {
            is AppSettingsCommand.LoadAppSettings -> appSettingsRepository
                .getAppSettings()
                .mapSuccessEvent { AppSettingsEvent.Internal.LoadAppSettingsSuccess(it) }
            is AppSettingsCommand.SetDarkThemeEnabled -> appSettingsRepository
                .changeAppSettings { copy(isDarkThemeEnabled = command.isEnabled) }
                .mapSuccessEvent(AppSettingsEvent.Internal::LoadAppSettingsSuccess)
            is AppSettingsCommand.SetSnowEnabled -> appSettingsRepository
                .changeAppSettings { copy(isSnowEnabled = command.isEnabled) }
                .mapSuccessEvent(AppSettingsEvent.Internal::LoadAppSettingsSuccess)
            is AppSettingsCommand.SetAutoHideBottomSheet -> appSettingsRepository
                .changeAppSettings { copy(autoHideBottomSheet = command.isEnabled) }
                .mapSuccessEvent(AppSettingsEvent.Internal::LoadAppSettingsSuccess)
            is AppSettingsCommand.ChangeLanguage -> appSettingsRepository
                .changeAppSettings { copy(languageCode = command.selectedLanguage) }
                .mapSuccessEvent(AppSettingsEvent.Internal::LoadAppSettingsSuccess)
            is AppSettingsCommand.ChangeMapType -> appSettingsRepository
                .changeAppSettings { copy(mapAppearanceType = command.selectedMapType) }
                .mapSuccessEvent(AppSettingsEvent.Internal::LoadAppSettingsSuccess)
            is AppSettingsCommand.SetShowQuickNavigationFab -> appSettingsRepository
                .changeAppSettings { copy(showNavigationButton = command.isVisible) }
                .mapSuccessEvent(AppSettingsEvent.Internal::LoadAppSettingsSuccess)

            is AppSettingsCommand.ObserveContributors -> Observable
                    .merge(
                        contributorsRepository.observeContributors(),
                        contributorsRepository.fetchContributors().toObservable(),
                    )
                    .mapSuccessEvent(AppSettingsEvent.Internal::ObserveContributorsSuccess)

            is AppSettingsCommand.ClearSelectedGroup -> Completable
                .fromAction { TODO("Just send user to onboarding") }
                .toObservable()
            is AppSettingsCommand.ChangeBackendEnvironment -> appSettingsRepository
                .changeAppSettings { copy(appEnvironment = command.appEnvironment) }
                .mapSuccessEvent(AppSettingsEvent.Internal::LoadAppSettingsSuccess)
        }
}
