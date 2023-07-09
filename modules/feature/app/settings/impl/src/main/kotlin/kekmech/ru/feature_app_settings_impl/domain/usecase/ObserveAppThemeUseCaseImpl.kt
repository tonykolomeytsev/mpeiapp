package kekmech.ru.feature_app_settings_impl.domain.usecase

import kekmech.ru.feature_app_settings_api.domain.model.AppTheme
import kekmech.ru.feature_app_settings_api.domain.usecase.ObserveAppThemeUseCase
import kekmech.ru.feature_app_settings_impl.data.AppSettingsRepositoryImpl
import kotlinx.coroutines.flow.StateFlow

internal class ObserveAppThemeUseCaseImpl(
    private val appSettingsRepositoryImpl: AppSettingsRepositoryImpl
) : ObserveAppThemeUseCase {

    override fun invoke(): StateFlow<AppTheme> =
        appSettingsRepositoryImpl.observeAppTheme()
}
