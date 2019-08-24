package kekmech.ru.core.dto

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.ForeignKey
import android.arch.persistence.room.PrimaryKey

@Entity(
    foreignKeys = [ForeignKey(
        entity = User::class,
        parentColumns = ["id"],
        childColumns = ["user_id"],
        onDelete = ForeignKey.CASCADE)],
    tableName = "schedules")
data class ScheduleNative(
    @PrimaryKey(autoGenerate = true) var id: Int = 0,
    @ColumnInfo(name = "user_id") var userId: Int = 0,
    @ColumnInfo(name = "calendar_week") var calendarWeek: Int? = null,
    @ColumnInfo(name = "university_week") var universityWeek: Int? = null,
    @ColumnInfo(name = "name") var name: String? = null
)