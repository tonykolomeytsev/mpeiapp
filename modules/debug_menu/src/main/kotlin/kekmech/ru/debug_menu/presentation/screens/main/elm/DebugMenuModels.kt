package kekmech.ru.debug_menu.presentation.screens.main.elm

import kekmech.ru.common_elm.Resource
import kekmech.ru.domain_app_settings_models.AppEnvironment
import vivid.money.elmslie.core.store.Store

internal typealias DebugMenuStore = Store<DebugMenuEvent, DebugMenuEffect, DebugMenuState>

internal data class DebugMenuState(
    val appEnvironment: Resource<AppEnvironment> = Resource.Loading,
)

internal sealed interface DebugMenuEvent {

    sealed interface Ui : DebugMenuEvent {

        object Init : Ui

        sealed interface Click : Ui {

            data class Environment(val appEnvironment: AppEnvironment) : Click
        }
    }

    sealed interface Internal : DebugMenuEvent {

        data class GetAppEnvironmentSuccess(val appEnvironment: AppEnvironment) : Internal
        data class SetAppEnvironmentSuccess(val appEnvironment: AppEnvironment) : Internal
    }
}

internal sealed interface DebugMenuCommand {

    object GetAppEnvironment : DebugMenuCommand
    data class SetAppEnvironment(val appEnvironment: AppEnvironment) : DebugMenuCommand
}

internal sealed interface DebugMenuEffect {

    object ReloadApp : DebugMenuEffect
}
