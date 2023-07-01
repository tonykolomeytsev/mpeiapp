package kekmech.ru.debug_menu.presentation.screens.main.elm

import vivid.money.elmslie.coroutines.ElmStoreCompat

internal class DebugMenuStoreFactory(
    private val actor: DebugMenuActor,
) {

    fun create(): DebugMenuStore =
        ElmStoreCompat(
            initialState = DebugMenuState(),
            reducer = DebugMenuReducer(),
            actor = actor,
            startEvent = DebugMenuEvent.Ui.Init,
        )
}
