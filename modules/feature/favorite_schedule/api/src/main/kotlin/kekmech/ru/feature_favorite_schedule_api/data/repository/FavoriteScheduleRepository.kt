package kekmech.ru.feature_favorite_schedule_api.data.repository

import kekmech.ru.feature_favorite_schedule_api.domain.model.FavoriteSchedule

interface FavoriteScheduleRepository {

    suspend fun updateOrInsertFavorite(favoriteSchedule: FavoriteSchedule)

    suspend fun getAllFavorites(): List<FavoriteSchedule>

    suspend fun deleteFavorite(favoriteSchedule: FavoriteSchedule)
}
