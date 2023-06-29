package kekmech.ru.debug_menu.presentation.screens.main.elm

import kekmech.ru.common_kotlin.Resource
import kekmech.ru.domain_app_settings_models.AppEnvironment
import vivid.money.elmslie.core.store.Store

internal typealias DebugMenuStore = Store<DebugMenuEvent, DebugMenuEffect, DebugMenuState>

internal data class DebugMenuState(
    val appEnvironment: Resource<AppEnvironment> = Resource.Loading,
)

internal sealed interface DebugMenuEvent {

    sealed interface Ui : DebugMenuEvent {

        object Init : Ui
    }

    sealed interface Internal : DebugMenuEvent
}

internal sealed interface DebugMenuCommand

internal sealed interface DebugMenuEffect
