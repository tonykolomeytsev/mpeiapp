package kekmech.ru.feature_app_settings.screens.main.elm

import io.reactivex.rxjava3.core.Observable
import kekmech.ru.domain_app_settings.AppSettings
import kekmech.ru.domain_app_settings.AppSettingsRepository
import kekmech.ru.domain_schedule.ScheduleRepository
import vivid.money.elmslie.core.store.Actor

internal class AppSettingsActor(
    private val appSettingsRepository: AppSettingsRepository,
    private val scheduleRepository: ScheduleRepository,
) : Actor<AppSettingsAction, AppSettingsEvent> {

    @Suppress("PARAMETER_NAME_CHANGED_ON_OVERRIDE")
    override fun execute(action: AppSettingsAction): Observable<AppSettingsEvent> =
        when (action) {
            is AppSettingsAction.LoadAppSettings -> Observable
                .just(appSettingsRepository as AppSettings)
                .mapSuccessEvent { AppSettingsEvent.News.AppSettingsLoaded(it) }
            is AppSettingsAction.SetDarkThemeEnabled -> appSettingsRepository
                .complete { isDarkThemeEnabled = action.isEnabled }
                .mapSuccessEvent(AppSettingsEvent.News.AppSettingsChanged)
            is AppSettingsAction.SetSnowEnabled -> appSettingsRepository
                .complete { isSnowEnabled = action.isEnabled }
                .mapSuccessEvent(AppSettingsEvent.News.AppSettingsChanged)
            is AppSettingsAction.SetAutoHideBottomSheet -> appSettingsRepository
                .complete { autoHideBottomSheet = action.isEnabled }
                .mapSuccessEvent(AppSettingsEvent.News.AppSettingsChanged)
            is AppSettingsAction.ClearSelectedGroup -> appSettingsRepository
                .complete { scheduleRepository.debugClearSelectedGroup() }
                .toObservable()
            is AppSettingsAction.ChangeBackendEnvironment -> appSettingsRepository
                .complete { isDebugEnvironment = action.isDebug }
                .toObservable()
            is AppSettingsAction.ChangeLanguage -> appSettingsRepository
                .complete { languageCode = action.selectedLanguage }
                .toObservable()
            is AppSettingsAction.ChangeMapType -> appSettingsRepository
                .complete { mapAppearanceType = action.selectedMapType }
                .toObservable()
            is AppSettingsAction.SetShowQuickNavigationFab -> appSettingsRepository
                .complete { showNavigationButton = action.isVisible }
                .toObservable()

            is AppSettingsAction.ObserveContributors -> Observable.merge(
                appSettingsRepository.fetchContributors().toObservable(),
                appSettingsRepository.observeContributors()
            )
                .mapSuccessEvent(AppSettingsEvent.News::ObserveContributorsSuccess)
        }
}