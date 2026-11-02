package kekmech.ru.mpeiapp.ui.main

import kekmech.ru.lib_navigation.BottomTab
import kekmech.ru.lib_navigation.BottomTabsSwitcher
import kekmech.ru.lib_navigation.DashboardTabKey
import kekmech.ru.lib_navigation.NewRoot
import kekmech.ru.lib_navigation.Router
import kekmech.ru.lib_navigation.TabScreenKey
import kekmech.ru.lib_navigation.screenKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow

class BottomTabsSwitcherImpl(
    private val router: Router,
) : BottomTabsSwitcher {

    private val reselectsFlow = MutableSharedFlow<BottomTab>(extraBufferCapacity = 1)

    override val reselects: Flow<BottomTab> = reselectsFlow.asSharedFlow()

    override fun changeTab(tab: BottomTab) {
        val currentTab = (router.backStack.lastOrNull() as? TabScreenKey)?.tab
        if (currentTab == tab) {
            reselectsFlow.tryEmit(tab)
            return
        }
        // Лента всегда лежит под остальными табами, чтобы "назад" с любого таба вело на нее
        if (tab == BottomTab.DASHBOARD) {
            router.executeCommand(NewRoot(DashboardTabKey))
        } else {
            router.executeCommand(NewRoot(DashboardTabKey, tab.screenKey))
        }
    }
}
