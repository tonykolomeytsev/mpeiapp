package kekmech.ru.lib_navigation

import kotlinx.coroutines.flow.Flow

public interface BottomTabsSwitcher {

    public fun changeTab(tab: BottomTab)

    public fun clearTab()

    public fun observe(): Flow<BottomTab?>
}
