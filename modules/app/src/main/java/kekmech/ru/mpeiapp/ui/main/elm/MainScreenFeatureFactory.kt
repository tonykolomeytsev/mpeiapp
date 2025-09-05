package kekmech.ru.mpeiapp.ui.main.elm

import money.vivid.elmslie.core.store.ElmStore
import money.vivid.elmslie.core.store.NoOpActor

object MainScreenFeatureFactory {

    fun create(): MainScreenStore = ElmStore(
        initialState = MainScreenState(),
        reducer = MainScreenReducer(),
        actor = NoOpActor()
    )
}
