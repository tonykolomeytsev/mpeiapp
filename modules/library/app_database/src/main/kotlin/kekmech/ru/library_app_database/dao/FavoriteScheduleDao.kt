package kekmech.ru.library_app_database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import kekmech.ru.library_app_database.entity.NormalFavoriteSchedule

@Dao
interface FavoriteScheduleDao {

    @Upsert
    suspend fun updateOrInsert(favoriteSchedule: NormalFavoriteSchedule)

    @Query("SELECT * FROM favorite_schedule")
    suspend fun getAll(): List<NormalFavoriteSchedule>

    @Delete
    suspend fun delete(favoriteSchedule: NormalFavoriteSchedule)
}
