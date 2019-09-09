package kekmech.ru.core.dto

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class User(
    @PrimaryKey(autoGenerate = true) var id: Int,
    @ColumnInfo(name = "first_launch") var firstLaunchFlag: Boolean, // первый запуск
    @ColumnInfo(name = "dev_mode") var developerMode: Boolean, // режим разработчика
    @ColumnInfo(name = "night_mode") var nightMode: Boolean,
    @ColumnInfo(name = "last_schedule_id") var lastScheduleId: Int // последнее открытое расписание
) {
    companion object {
        fun defaultUser() = User(
            0,
            firstLaunchFlag = false,
            developerMode = false,
            nightMode = false,
            lastScheduleId = -1
        )
    }
}