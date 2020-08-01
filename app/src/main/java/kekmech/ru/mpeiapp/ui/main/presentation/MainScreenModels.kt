package kekmech.ru.mpeiapp.ui.main.presentation

import kekmech.ru.common_mvi.Feature

typealias MainScreenFeature = Feature<MainScreenState, MainScreenEvent, MainScreenEffect>

data class MainScreenState(
    val isLoading: Boolean = false
)

sealed class MainScreenEvent {

    // events from ui
    sealed class Wish : MainScreenEvent() {
        object Init : Wish()
    }

    // events from actor
    sealed class News : MainScreenEvent() {

    }
}

sealed class MainScreenAction

sealed class MainScreenEffect