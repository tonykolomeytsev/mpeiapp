package kekmech.ru.core.dto

import androidx.room.*

@Entity(
//    foreignKeys = [ForeignKey(
//        entity = User::class,
//        parentColumns = ["id"],
//        childColumns = ["user_id"])],
    tableName = "schedules",
    indices = [Index(
        value = ["id", "user_id"],
        unique = true
    )])
data class ScheduleNative(
    @ColumnInfo(name = "user_id") var userId: Int,
    @ColumnInfo(name = "group") var group: String,
    @ColumnInfo(name = "calendar_week") var calendarWeek: Int,
    @ColumnInfo(name = "university_week") var universityWeek: Int,
    @ColumnInfo(name = "tag") var name: String
) {

    @PrimaryKey(autoGenerate = true) var id: Int = 0
}