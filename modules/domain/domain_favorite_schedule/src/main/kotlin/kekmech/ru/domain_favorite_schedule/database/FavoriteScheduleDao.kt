package kekmech.ru.domain_favorite_schedule.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single
import kekmech.ru.domain_favorite_schedule.database.entities.NormalFavoriteSchedule

@Dao
public interface FavoriteScheduleDao {

    @Upsert
    public fun updateOrInsert(favoriteSchedule: NormalFavoriteSchedule): Completable

    @Query("SELECT * FROM favorite_schedule")
    public fun getAll(): Single<List<NormalFavoriteSchedule>>

    @Delete
    public fun delete(favoriteSchedule: NormalFavoriteSchedule): Completable
}
