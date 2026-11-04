package kekmech.ru.feature_dashboard_impl.navigation

import androidx.navigation3.runtime.EntryProviderScope
import kekmech.ru.feature_dashboard_impl.presentation.screen.main.DashboardEntry
import kekmech.ru.lib_navigation.DashboardTabKey
import kekmech.ru.lib_navigation.NavEntries
import kekmech.ru.lib_navigation.ScreenKey
import kekmech.ru.lib_navigation.tabEntry

internal class DashboardNavEntries : NavEntries {

    override fun EntryProviderScope<ScreenKey>.register() {
        tabEntry(DashboardTabKey) { DashboardEntry() }
    }
}
