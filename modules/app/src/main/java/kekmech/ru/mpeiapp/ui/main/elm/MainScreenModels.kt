package kekmech.ru.mpeiapp.ui.main.elm

import money.vivid.elmslie.core.store.Store

typealias MainScreenStore = Store<MainScreenEvent, MainScreenEffect, MainScreenState>

data class MainScreenState(
    val isLoading: Boolean = false
)

sealed class MainScreenEvent {

    // events from ui
    sealed class Wish : MainScreenEvent() {
        object Init : Wish()
    }

    // events from actor
    sealed class News : MainScreenEvent()
}

sealed class MainScreenAction

sealed class MainScreenEffect
