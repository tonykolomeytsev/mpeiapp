package kekmech.ru.feature_app_settings.screens.main.elm

import io.reactivex.rxjava3.core.Observable
import kekmech.ru.domain_app_settings.AppSettings
import kekmech.ru.domain_app_settings.AppSettingsRepository
import kekmech.ru.domain_schedule.ScheduleRepository
import vivid.money.elmslie.core.store.Actor

internal class AppSettingsActor(
    private val appSettingsRepository: AppSettingsRepository,
    private val scheduleRepository: ScheduleRepository,
) : Actor<AppSettingsCommand, AppSettingsEvent> {

    override fun execute(command: AppSettingsCommand): Observable<AppSettingsEvent> =
        when (command) {
            is AppSettingsCommand.LoadAppSettings -> Observable
                .just(appSettingsRepository as AppSettings)
                .mapSuccessEvent { AppSettingsEvent.Internal.AppSettingsLoaded(it) }
            is AppSettingsCommand.SetDarkThemeEnabled -> appSettingsRepository
                .complete { isDarkThemeEnabled = command.isEnabled }
                .mapSuccessEvent(AppSettingsEvent.Internal.AppSettingsChanged)
            is AppSettingsCommand.SetSnowEnabled -> appSettingsRepository
                .complete { isSnowEnabled = command.isEnabled }
                .mapSuccessEvent(AppSettingsEvent.Internal.AppSettingsChanged)
            is AppSettingsCommand.SetAutoHideBottomSheet -> appSettingsRepository
                .complete { autoHideBottomSheet = command.isEnabled }
                .mapSuccessEvent(AppSettingsEvent.Internal.AppSettingsChanged)
            is AppSettingsCommand.ClearSelectedGroup -> appSettingsRepository
                .complete { scheduleRepository.debugClearSelectedGroup() }
                .toObservable()
            is AppSettingsCommand.ChangeBackendEnvironment -> appSettingsRepository
                .complete { isDebugEnvironment = command.isDebug }
                .toObservable()
            is AppSettingsCommand.ChangeLanguage -> appSettingsRepository
                .complete { languageCode = command.selectedLanguage }
                .toObservable()
            is AppSettingsCommand.ChangeMapType -> appSettingsRepository
                .complete { mapAppearanceType = command.selectedMapType }
                .toObservable()
            is AppSettingsCommand.SetShowQuickNavigationFab -> appSettingsRepository
                .complete { showNavigationButton = command.isVisible }
                .toObservable()

            is AppSettingsCommand.ObserveContributors ->
                appSettingsRepository.getContributors()
                    .mapSuccessEvent(AppSettingsEvent.Internal::ObserveContributorsSuccess)
        }
}
