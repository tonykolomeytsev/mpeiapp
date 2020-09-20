package kekmech.ru.feature_app_settings.presentation

import kekmech.ru.common_mvi.Feature
import kekmech.ru.feature_app_settings.items.SettingsItem

typealias AppSettingsFeature = Feature<AppSettingsState, AppSettingsEvent, AppSettingsEffect>

data class AppSettingsState(
    val items: List<SettingsItem> = emptyList()
)

sealed class AppSettingsEvent {

    sealed class Wish : AppSettingsEvent() {

        object Init : Wish()

        object Action {

        }
    }

    sealed class News : AppSettingsEvent() {

    }
}

sealed class AppSettingsAction {

}

sealed class AppSettingsEffect {

}