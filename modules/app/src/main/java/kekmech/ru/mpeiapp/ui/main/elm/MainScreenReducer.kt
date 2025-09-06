package kekmech.ru.mpeiapp.ui.main.elm

import money.vivid.elmslie.core.store.Result
import money.vivid.elmslie.core.store.StateReducer

typealias MainScreenResult = Result<MainScreenState, MainScreenEffect, MainScreenAction>

class MainScreenReducer : StateReducer<MainScreenEvent, MainScreenState, MainScreenEffect, MainScreenAction>() {

    override fun StateReducer<MainScreenEvent, MainScreenState, MainScreenEffect, MainScreenAction>.Result.reduce(
        event: MainScreenEvent
    ) {
        /* no-op */
    }
}
