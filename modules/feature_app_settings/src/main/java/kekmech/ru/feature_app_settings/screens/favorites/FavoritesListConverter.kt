package kekmech.ru.feature_app_settings.screens.favorites

import android.content.Context
import kekmech.ru.coreui.items.AddActionItem
import kekmech.ru.coreui.items.FavoriteScheduleItem
import kekmech.ru.coreui.items.SpaceItem
import kekmech.ru.feature_app_settings.R
import kekmech.ru.feature_app_settings.screens.favorites.mvi.FavoritesState

internal class FavoritesListConverter(context: Context) {

    private val addFavoriteItem = AddActionItem(
        context.getString(R.string.favorites_empty_state_add)
    )

    fun map(state: FavoritesState): List<Any> = when {
        state.favorites == null -> emptyList() // loading
        state.favorites.isEmpty() -> listOf(addFavoriteItem)
        else -> mutableListOf<Any>().apply {
            addAll(state.favorites.map(::FavoriteScheduleItem))
            add(SpaceItem.VERTICAL_24)
            add(addFavoriteItem)
        }
    }
}