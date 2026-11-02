package kekmech.ru.feature_app_settings_impl.navigation

import androidx.navigation3.runtime.EntryProviderScope
import kekmech.ru.feature_app_settings_impl.presentation.screens.edit_favorite.EditFavoriteEntry
import kekmech.ru.feature_app_settings_impl.presentation.screens.favorites.FavoritesEntry
import kekmech.ru.feature_app_settings_impl.presentation.screens.main.AppSettingsEntry
import kekmech.ru.lib_navigation.NavEntries
import kekmech.ru.lib_navigation.ScreenKey

internal class AppSettingsNavEntries : NavEntries {

    override fun EntryProviderScope<ScreenKey>.register() {
        entry<AppSettingsScreenKey> { AppSettingsEntry() }
        entry<FavoritesScreenKey> { FavoritesEntry() }
        entry<EditFavoriteScreenKey> { EditFavoriteEntry(it) }
    }
}
