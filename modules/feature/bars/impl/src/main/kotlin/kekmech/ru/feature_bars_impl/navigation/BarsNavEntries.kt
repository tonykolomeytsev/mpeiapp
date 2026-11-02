package kekmech.ru.feature_bars_impl.navigation

import androidx.navigation3.runtime.EntryProviderScope
import kekmech.ru.feature_bars_impl.presentation.screen.login.BarsLoginEntry
import kekmech.ru.feature_bars_impl.presentation.screen.main_compose.BarsEntry
import kekmech.ru.lib_navigation.NavEntries
import kekmech.ru.lib_navigation.ProfileTabKey
import kekmech.ru.lib_navigation.ScreenKey
import kekmech.ru.lib_navigation.tabEntry

internal class BarsNavEntries : NavEntries {

    override fun EntryProviderScope<ScreenKey>.register() {
        tabEntry(ProfileTabKey) { BarsEntry() }
        entry<BarsLoginScreenKey> { BarsLoginEntry() }
    }
}
