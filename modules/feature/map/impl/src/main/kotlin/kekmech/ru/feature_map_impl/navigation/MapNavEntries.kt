package kekmech.ru.feature_map_impl.navigation

import androidx.navigation3.runtime.EntryProviderScope
import com.kekmech.lib_fragment.TabFragment
import kekmech.ru.feature_map_impl.presentation.screen.main.MapFragment
import kekmech.ru.lib_navigation.BottomTab
import kekmech.ru.lib_navigation.MapTabKey
import kekmech.ru.lib_navigation.NavEntries
import kekmech.ru.lib_navigation.ScreenKey
import kekmech.ru.lib_navigation.tabEntry

internal class MapNavEntries : NavEntries {

    override fun EntryProviderScope<ScreenKey>.register() {
        tabEntry(MapTabKey) { TabFragment<MapFragment>(BottomTab.MAP) }
    }
}
