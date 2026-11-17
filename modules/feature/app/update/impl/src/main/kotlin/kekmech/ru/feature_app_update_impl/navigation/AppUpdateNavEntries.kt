package kekmech.ru.feature_app_update_impl.navigation

import androidx.navigation3.runtime.EntryProviderScope
import kekmech.ru.feature_app_update_impl.presentation.BlockingUpdateEntry
import kekmech.ru.feature_app_update_impl.presentation.ForceUpdateEntry
import kekmech.ru.lib_navigation.NavEntries
import kekmech.ru.lib_navigation.ScreenKey
import kekmech.ru.lib_navigation.bottomSheetEntry

internal class AppUpdateNavEntries : NavEntries {

    override fun EntryProviderScope<ScreenKey>.register() {
        entry<BlockingUpdateScreenKey> { BlockingUpdateEntry(it) }
        bottomSheetEntry<ForceUpdateScreenKey>(skipPartiallyExpanded = true, isBare = true) {
            ForceUpdateEntry(it)
        }
    }
}
