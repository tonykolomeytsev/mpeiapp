package kekmech.ru.lib_navigation

import kotlinx.coroutines.flow.Flow

interface BottomTabsSwitcher {

    fun changeTab(tab: BottomTab)

    fun clearTab()

    fun observe(): Flow<BottomTab?>
}
