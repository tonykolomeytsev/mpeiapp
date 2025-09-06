package kekmech.ru.debug_menu.presentation.screens.main.elm

import kekmech.ru.lib_app_info.AppVersionName
import money.vivid.elmslie.core.store.ElmStore

internal class DebugMenuStoreFactory(
    private val actor: DebugMenuActor,
    private val appVersionName: AppVersionName,
) {

    fun create(): DebugMenuStore =
        ElmStore(
            initialState = DebugMenuState(
                appVersionName = appVersionName,
            ),
            reducer = DebugMenuReducer(),
            actor = actor,
            startEvent = DebugMenuEvent.Ui.Init,
        )
}
