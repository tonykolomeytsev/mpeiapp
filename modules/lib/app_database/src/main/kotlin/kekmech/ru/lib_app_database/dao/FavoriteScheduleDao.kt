package kekmech.ru.lib_app_database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import kekmech.ru.lib_app_database.entity.NormalFavoriteSchedule

@Dao
public interface FavoriteScheduleDao {

    @Upsert
    public suspend fun updateOrInsert(favoriteSchedule: NormalFavoriteSchedule)

    @Query("SELECT * FROM favorite_schedule")
    public suspend fun getAll(): List<NormalFavoriteSchedule>

    @Delete
    public suspend fun delete(favoriteSchedule: NormalFavoriteSchedule)
}
