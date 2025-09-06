package kekmech.ru.mpeiapp.ui.main.elm

import money.vivid.elmslie.core.store.ElmStore
import money.vivid.elmslie.core.store.NoOpActor

object MainScreenStoreFactory {

    fun create(): MainScreenStore = ElmStore(
        initialState = MainScreenState(),
        reducer = MainScreenReducer(),
        actor = NoOpActor(),
        startEvent = MainScreenEvent.Wish.Init
    )
}
