package kekmech.ru.domain_favorite_schedule

import kekmech.ru.domain_favorite_schedule.database.FavoriteScheduleDao
import kekmech.ru.domain_favorite_schedule.database.entities.toDomain
import kekmech.ru.domain_favorite_schedule.database.entities.toNormal
import kekmech.ru.domain_favorite_schedule.dto.FavoriteSchedule

class FavoriteScheduleRepository(
    private val favoriteScheduleDao: FavoriteScheduleDao,
) {

    suspend fun updateOrInsertFavorite(favoriteSchedule: FavoriteSchedule) =
        favoriteScheduleDao.updateOrInsert(favoriteSchedule.toNormal())

    suspend fun getAllFavorites(): List<FavoriteSchedule> =
        favoriteScheduleDao
            .getAll()
            .map { it.toDomain() }

    suspend fun deleteFavorite(favoriteSchedule: FavoriteSchedule) =
        favoriteScheduleDao.delete(favoriteSchedule.toNormal())
}
