package kekmech.ru.feature_bars_impl.navigation

import androidx.navigation3.runtime.EntryProviderScope
import kekmech.ru.feature_bars_impl.presentation.screen.accounts.BarsAccountsEntry
import kekmech.ru.feature_bars_impl.presentation.screen.details.BarsDetailsScreen
import kekmech.ru.feature_bars_impl.presentation.screen.login.BarsLoginEntry
import kekmech.ru.feature_bars_impl.presentation.screen.main.BarsEntry
import kekmech.ru.lib_navigation.NavEntries
import kekmech.ru.lib_navigation.ProfileTabKey
import kekmech.ru.lib_navigation.ScreenKey
import kekmech.ru.lib_navigation.bottomSheetEntry
import kekmech.ru.lib_navigation.tabEntry

internal class BarsNavEntries : NavEntries {

    override fun EntryProviderScope<ScreenKey>.register() {
        tabEntry(ProfileTabKey) { BarsEntry() }
        bottomSheetEntry<BarsAccountsScreenKey> { BarsAccountsEntry() }
        entry<BarsLoginScreenKey> { BarsLoginEntry(it) }
        bottomSheetEntry<BarsDetailsScreenKey> { BarsDetailsScreen(it) }
    }
}
