package kekmech.ru.feature_app_settings.presentation

import io.reactivex.Observable
import kekmech.ru.common_mvi.Actor
import kekmech.ru.domain_app_settings.AppSettingsRepository

class AppSettingsActor(
    private val appSettingsRepository: AppSettingsRepository
) : Actor<AppSettingsAction, AppSettingsEvent> {

    override fun execute(action: AppSettingsAction): Observable<AppSettingsEvent> = when (action) {
        else -> TODO()
    }
}