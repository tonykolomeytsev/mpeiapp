package kekmech.ru.feature_app_settings.presentation

import io.reactivex.Observable
import kekmech.ru.common_mvi.Actor
import kekmech.ru.domain_app_settings.AppSettings
import kekmech.ru.domain_app_settings.AppSettingsRepository

internal class AppSettingsActor(
    private val appSettingsRepository: AppSettingsRepository
) : Actor<AppSettingsAction, AppSettingsEvent> {

    override fun execute(action: AppSettingsAction): Observable<AppSettingsEvent> = when (action) {
        is AppSettingsAction.LoadAppSettings -> Observable
            .just(appSettingsRepository as AppSettings)
            .mapSuccessEvent(withSkippingError = true) { AppSettingsEvent.News.AppSettingsLoaded(it) }
        is AppSettingsAction.SetDarkThemeEnabled -> appSettingsRepository
            .complete { isDarkThemeEnabled = action.isEnabled }
            .mapSuccessEvent(AppSettingsEvent.News.AppSettingsChanged)
        is AppSettingsAction.SetChangeDayAfterChangeWeek -> appSettingsRepository
            .complete { changeDayAfterChangeWeek = action.isEnabled }
            .mapSuccessEvent(AppSettingsEvent.News.AppSettingsChanged)
        is AppSettingsAction.SetAutoHideBottomSheet -> appSettingsRepository
            .complete { autoHideBottomSheet = action.isEnabled }
            .mapSuccessEvent(AppSettingsEvent.News.AppSettingsChanged)
    }
}