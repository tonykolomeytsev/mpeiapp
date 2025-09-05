package kekmech.ru.domain_favorite_schedule

import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single
import kekmech.ru.common_kotlin.fromBase64
import kekmech.ru.common_kotlin.toBase64
import kekmech.ru.domain_favorite_schedule.database.FavoriteScheduleDao
import kekmech.ru.domain_favorite_schedule.database.entities.NormalFavoriteSchedule
import kekmech.ru.domain_favorite_schedule.dto.FavoriteSchedule

public class FavoriteScheduleRepository(
    private val favoriteScheduleDao: FavoriteScheduleDao,
) {

    public fun updateOrInsertFavorite(favoriteSchedule: FavoriteSchedule): Completable =
        favoriteScheduleDao.updateOrInsert(toNormal(favoriteSchedule))

    public fun getAllFavorites(): Single<List<FavoriteSchedule>> =
        favoriteScheduleDao
            .getAll()
            .map { it.map(::fromNormal) }

    public fun deleteFavorite(favoriteSchedule: FavoriteSchedule): Completable =
        favoriteScheduleDao.delete(toNormal(favoriteSchedule))

    private fun toNormal(favoriteSchedule: FavoriteSchedule): NormalFavoriteSchedule =
        NormalFavoriteSchedule(
            name = favoriteSchedule.name,
            type = favoriteSchedule.type,
            description = favoriteSchedule.description.toBase64(),
            order = favoriteSchedule.order,
        )

    private fun fromNormal(normalFavoriteSchedule: NormalFavoriteSchedule): FavoriteSchedule =
        FavoriteSchedule(
            name = normalFavoriteSchedule.name,
            type = normalFavoriteSchedule.type,
            description = normalFavoriteSchedule.description.fromBase64(),
            order = normalFavoriteSchedule.order,
        )
}
