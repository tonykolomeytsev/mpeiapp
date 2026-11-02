package kekmech.ru.feature_search_impl.navigation

import androidx.navigation3.runtime.EntryProviderScope
import kekmech.ru.feature_search_impl.screens.main.SearchEntry
import kekmech.ru.lib_navigation.NavEntries
import kekmech.ru.lib_navigation.ScreenKey

internal class SearchNavEntries : NavEntries {

    override fun EntryProviderScope<ScreenKey>.register() {
        entry<SearchScreenKey> { SearchEntry(it) }
    }
}
