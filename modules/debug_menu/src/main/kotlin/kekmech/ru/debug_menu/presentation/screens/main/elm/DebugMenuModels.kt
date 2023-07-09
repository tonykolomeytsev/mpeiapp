package kekmech.ru.debug_menu.presentation.screens.main.elm

import kekmech.ru.feature_app_settings_api.domain.model.AppTheme
import kekmech.ru.lib_app_info.AppVersionName
import kekmech.ru.lib_elm.Resource
import kekmech.ru.lib_network.AppEnvironment
import vivid.money.elmslie.core.store.Store

internal typealias DebugMenuStore = Store<DebugMenuEvent, DebugMenuEffect, DebugMenuState>

internal data class DebugMenuState(
    val appEnvironment: Resource<AppEnvironment> = Resource.Loading,
    val appVersionName: AppVersionName,
    val appTheme: Resource<AppTheme> = Resource.Loading,
)

internal sealed interface DebugMenuEvent {

    sealed interface Ui : DebugMenuEvent {

        object Init : Ui

        sealed interface Click : Ui {

            data class Environment(val appEnvironment: AppEnvironment) : Click
            data class Theme(val appTheme: AppTheme) : Click
        }
    }

    sealed interface Internal : DebugMenuEvent {

        data class GetAppEnvironmentSuccess(val appEnvironment: AppEnvironment) : Internal
        data class SetAppEnvironmentSuccess(val appEnvironment: AppEnvironment) : Internal
        data class GetAppThemeSuccess(val appTheme: AppTheme) : Internal
        data class SetAppThemeSuccess(val appTheme: AppTheme) : Internal
    }
}

internal sealed interface DebugMenuCommand {

    object GetAppEnvironment : DebugMenuCommand
    object GetAppTheme : DebugMenuCommand
    data class SetAppEnvironment(val appEnvironment: AppEnvironment) : DebugMenuCommand
    data class SetAppTheme(val appTheme: AppTheme) : DebugMenuCommand
}

internal sealed interface DebugMenuEffect {

    object ReloadApp : DebugMenuEffect
}
