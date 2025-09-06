package kekmech.ru.feature_favorite_schedule_api.data.repository

import kekmech.ru.feature_favorite_schedule_api.domain.model.FavoriteSchedule

public interface FavoriteScheduleRepository {

    public suspend fun updateOrInsertFavorite(favoriteSchedule: FavoriteSchedule)

    public suspend fun getAllFavorites(): List<FavoriteSchedule>

    public suspend fun deleteFavorite(favoriteSchedule: FavoriteSchedule)
}
