package kekmech.ru.feature_search_impl.navigation

import kekmech.ru.lib_navigation.ScreenKey
import kotlinx.parcelize.Parcelize

@Parcelize
internal data class SearchScreenKey(
    val query: String,
    val filter: String = "ALL",
) : ScreenKey
