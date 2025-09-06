package kekmech.ru.mpeiapp.demo.screens.elmslie.elm

import money.vivid.elmslie.core.store.ElmStore
internal class ElmDemoStoreFactory {

    fun create(randomArgument: Int): ElmDemoStore =
        ElmStoreCompat(
            initialState = ElmDemoState(randomArgument = randomArgument),
            reducer = ElmDemoReducer(),
            actor = ElmDemoActor(),
            startEvent = ElmDemoEvent.Ui.Init,
        )
}
