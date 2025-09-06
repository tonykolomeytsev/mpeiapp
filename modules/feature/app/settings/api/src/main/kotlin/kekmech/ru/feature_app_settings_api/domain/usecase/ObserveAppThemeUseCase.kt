package kekmech.ru.feature_app_settings_api.domain.usecase

import kekmech.ru.feature_app_settings_api.domain.model.AppTheme
import kotlinx.coroutines.flow.StateFlow

public interface ObserveAppThemeUseCase {

    public operator fun invoke(): StateFlow<AppTheme>
}
