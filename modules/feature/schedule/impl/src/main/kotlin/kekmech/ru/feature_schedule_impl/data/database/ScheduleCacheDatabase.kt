package kekmech.ru.feature_schedule_impl.data.database

import androidx.room.ColumnInfo
import androidx.room.Dao
import androidx.room.Database
import androidx.room.Entity
import androidx.room.Query
import androidx.room.RoomDatabase
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

/**
 * Downloaded schedules. Separate from the app database (`mpeix.db`) on purpose: it holds
 * nothing but a cache, so on any schema change it's dropped and downloaded again instead of
 * being migrated, and user data (notes, favorites) is never at risk.
 *
 * Bump [Version] on every change of [ScheduleWeekEntity], no migrations needed.
 */
@Database(
    entities = [ScheduleWeekEntity::class],
    version = ScheduleCacheDatabase.Version,
    exportSchema = false,
)
internal abstract class ScheduleCacheDatabase : RoomDatabase() {

    abstract fun scheduleWeekDao(): ScheduleWeekDao

    companion object {

        const val Version = 1
        const val Name = "schedule_cache.db"
    }
}

@Entity(
    tableName = "schedule_week",
    primaryKeys = ["type", "name", "first_day_of_week"],
)
internal data class ScheduleWeekEntity(
    /** [kekmech.ru.feature_schedule_api.domain.model.ScheduleType.name] */
    @ColumnInfo(name = "type") val type: String,
    /** Schedule name in upper case */
    @ColumnInfo(name = "name") val name: String,
    /** [java.time.LocalDate.toEpochDay] of the week's Monday */
    @ColumnInfo(name = "first_day_of_week") val firstDayOfWeek: Long,
    /** Backend response ([kekmech.ru.feature_schedule_impl.data.model.ScheduleDto]) as JSON */
    @ColumnInfo(name = "payload") val payload: String,
    /** When the payload was downloaded, epoch millis */
    @ColumnInfo(name = "updated_at") val updatedAt: Long,
)

@Dao
internal interface ScheduleWeekDao {

    @Query(
        "SELECT * FROM schedule_week " +
            "WHERE type = :type AND name = :name AND first_day_of_week = :firstDayOfWeek",
    )
    fun observe(type: String, name: String, firstDayOfWeek: Long): Flow<ScheduleWeekEntity?>

    @Query(
        "SELECT updated_at FROM schedule_week " +
            "WHERE type = :type AND name = :name AND first_day_of_week = :firstDayOfWeek",
    )
    suspend fun getUpdatedAt(type: String, name: String, firstDayOfWeek: Long): Long?

    @Upsert
    suspend fun upsert(entity: ScheduleWeekEntity)

    /** Delete weeks that ended long ago and weeks nobody has opened for a long time */
    @Query(
        "DELETE FROM schedule_week " +
            "WHERE first_day_of_week < :firstDayOfWeekBefore OR updated_at < :updatedBefore",
    )
    suspend fun deleteOutdated(firstDayOfWeekBefore: Long, updatedBefore: Long)
}
