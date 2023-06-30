package kekmech.ru.mpeiapp.demo.screens.elmslie.elm

import vivid.money.elmslie.coroutines.ElmStoreCompat

internal class ElmDemoStoreFactory {

    fun create(randomArgument: Int): ElmDemoStore =
        ElmStoreCompat(
            initialState = ElmDemoState(randomArgument = randomArgument),
            reducer = ElmDemoReducer(),
            actor = ElmDemoActor(),
            startEvent = ElmDemoEvent.Ui.Init,
        )
}
