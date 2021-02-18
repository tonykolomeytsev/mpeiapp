package kekmech.ru.mpeiapp.ui.main.elm

import vivid.money.elmslie.core.store.StateReducer
import vivid.money.elmslie.core.store.Result

typealias MainScreenResult = Result<MainScreenState, MainScreenEffect, MainScreenAction>

class MainScreenReducer : StateReducer<MainScreenEvent, MainScreenState, MainScreenEffect, MainScreenAction> {

    override fun reduce(
        event: MainScreenEvent,
        state: MainScreenState
    ): MainScreenResult = Result(state = state.copy())
}