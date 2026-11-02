package kekmech.ru.feature_app_settings_impl.navigation

import kekmech.ru.feature_favorite_schedule_api.domain.model.FavoriteSchedule
import kekmech.ru.lib_navigation.ScreenKey
import kotlinx.parcelize.Parcelize

@Parcelize
internal data object AppSettingsScreenKey : ScreenKey

@Parcelize
internal data object FavoritesScreenKey : ScreenKey

@Parcelize
internal data class EditFavoriteScreenKey(
    val favoriteSchedule: FavoriteSchedule,
    val resultKey: String,
) : ScreenKey
