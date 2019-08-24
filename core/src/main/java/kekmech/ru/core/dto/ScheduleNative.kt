package kekmech.ru.core.dto

import android.arch.persistence.room.*

@Entity(
    foreignKeys = [ForeignKey(
        entity = User::class,
        parentColumns = ["id"],
        childColumns = ["user_id"])],
    tableName = "schedules",
    indices = [Index(
        value = ["id", "user_id"],
        unique = true
    )])
data class ScheduleNative @Ignore constructor(
    @PrimaryKey(autoGenerate = true) var id: Int = 0,
    @ColumnInfo(name = "user_id") var userId: Int = 0,
    @ColumnInfo(name = "calendar_week") var calendarWeek: Int? = null,
    @ColumnInfo(name = "university_week") var universityWeek: Int? = null,
    @ColumnInfo(name = "name") var name: String? = null
) {
    constructor() : this(0) // Чтобы скрыть WARNING при компиляции
}