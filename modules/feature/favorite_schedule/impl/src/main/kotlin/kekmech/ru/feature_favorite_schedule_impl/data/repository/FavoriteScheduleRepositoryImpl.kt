package kekmech.ru.feature_favorite_schedule_impl.data.repository

import kekmech.ru.feature_favorite_schedule_api.data.repository.FavoriteScheduleRepository
import kekmech.ru.feature_favorite_schedule_api.domain.model.FavoriteSchedule
import kekmech.ru.feature_favorite_schedule_impl.data.database.mapper.toDomain
import kekmech.ru.feature_favorite_schedule_impl.data.database.mapper.toNormal
import kekmech.ru.feature_schedule_api.ScheduleSyncScheduler
import kekmech.ru.lib_app_database.api.reportingSQLiteErrors
import kekmech.ru.lib_app_database.dao.FavoriteScheduleDao

internal class FavoriteScheduleRepositoryImpl(
    private val favoriteScheduleDao: FavoriteScheduleDao,
    private val scheduleSyncScheduler: ScheduleSyncScheduler,
) : FavoriteScheduleRepository {

    override suspend fun updateOrInsertFavorite(favoriteSchedule: FavoriteSchedule) {
        reportingSQLiteErrors("favorite_schedule.updateOrInsert") {
            favoriteScheduleDao.updateOrInsert(favoriteSchedule.toNormal())
        }
        // make the favorite available offline right away, not only after the nightly sync
        scheduleSyncScheduler.requestSync()
    }

    override suspend fun getAllFavorites(): List<FavoriteSchedule> =
        reportingSQLiteErrors("favorite_schedule.getAll") {
            favoriteScheduleDao
                .getAll()
                .map { it.toDomain() }
        }

    override suspend fun deleteFavorite(favoriteSchedule: FavoriteSchedule) =
        reportingSQLiteErrors("favorite_schedule.delete") {
            favoriteScheduleDao.delete(favoriteSchedule.toNormal())
        }
}
