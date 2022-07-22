package kekmech.ru.mpeiapp.ui.main.elm

import vivid.money.elmslie.core.store.ElmStore
import vivid.money.elmslie.core.store.NoOpActor

object MainScreenFeatureFactory {

    fun create(): MainScreenStore = ElmStore(
        initialState = MainScreenState(),
        reducer = MainScreenReducer(),
        actor = NoOpActor()
    )
}
