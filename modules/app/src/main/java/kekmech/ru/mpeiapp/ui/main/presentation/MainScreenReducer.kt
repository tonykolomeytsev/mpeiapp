package kekmech.ru.mpeiapp.ui.main.presentation

import kekmech.ru.common_mvi.BaseReducer
import kekmech.ru.common_mvi.Result

typealias MainScreenResult = Result<MainScreenState, MainScreenEffect, MainScreenAction>

class MainScreenReducer : BaseReducer<MainScreenState, MainScreenEvent, MainScreenEffect, MainScreenAction> {

    override fun reduce(
        event: MainScreenEvent,
        state: MainScreenState
    ): MainScreenResult = Result(state = state.copy())
}