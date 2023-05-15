package kekmech.ru.domain_favorite_schedule

import kekmech.ru.domain_favorite_schedule.database.FavoriteScheduleDao
import kekmech.ru.domain_favorite_schedule.database.entities.NormalFavoriteSchedule
import kekmech.ru.domain_favorite_schedule.database.entities.toDomain
import kekmech.ru.domain_favorite_schedule.database.entities.toNormal
import kekmech.ru.domain_favorite_schedule.dto.FavoriteSchedule
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

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
