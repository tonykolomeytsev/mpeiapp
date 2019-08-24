package kekmech.ru.core.dto

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.Ignore
import android.arch.persistence.room.PrimaryKey

@Entity(tableName = "users")
data class User @Ignore constructor(
    @PrimaryKey(autoGenerate = true) var id: Int = 0,
    @ColumnInfo(name = "first_launch") var firstLaunchFlag: Boolean? = null, // первый запуск
    @ColumnInfo(name = "dev_mode") var developerMode: Boolean? = null, // режим разработчика
    @ColumnInfo(name = "night_mode") var nightMode: Boolean? = null,
    @ColumnInfo(name = "last_schedule_id") var lastScheduleId: Int = 0 // последнее открытое расписание
) {
    constructor() : this(0) // Чтобы скрыть WARNING при компиляции

    companion object {
        fun defaultUser() = User(
            0,
            firstLaunchFlag = false,
            developerMode = false,
            nightMode = false
        )
    }
}