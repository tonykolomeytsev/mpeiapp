package kekmech.ru.domain_favorite_schedule

import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single
import kekmech.ru.common_app_database.AppDatabase
import kekmech.ru.domain_favorite_schedule.dto.FavoriteSchedule

class FavoriteScheduleRepository(
    appDatabase: AppDatabase,
) {

    private val favoriteSource: FavoriteScheduleSource =
        FavoriteScheduleSource(appDatabase)

    fun getFavorites(): Single<List<FavoriteSchedule>> =
        Single.just(favoriteSource.getAll())

    fun setFavorites(favorites: List<FavoriteSchedule>): Completable =
        Completable.fromRunnable {
            favoriteSource.deleteAll()
            favoriteSource.addAll(favorites)
        }

    fun addFavorite(favoriteSchedule: FavoriteSchedule): Completable =
        Completable.fromRunnable {
            favoriteSource.add(favoriteSchedule)
        }

    fun removeFavorite(favoriteSchedule: FavoriteSchedule): Completable =
        Completable.fromRunnable {
            favoriteSource.remove(favoriteSchedule)
        }
}
