package kekmech.ru.domain_favorite_schedule.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import kekmech.ru.domain_schedule_models.dto.ScheduleType

@Entity(tableName = "favorite_schedule")
@TypeConverters(NormalFavoriteScheduleTypeConverter::class)
data class NormalFavoriteSchedule(
    @PrimaryKey
    val name: String,
    val type: ScheduleType,
    val description: String,
    val order: Int,
)

@Suppress("unused")
internal class NormalFavoriteScheduleTypeConverter {

    @TypeConverter
    fun fromScheduleType(string: String): ScheduleType =
        ScheduleType.values().first { it.name.equals(string, ignoreCase = true) }

    @TypeConverter
    fun toScheduleType(scheduleType: ScheduleType): String = scheduleType.name
}
