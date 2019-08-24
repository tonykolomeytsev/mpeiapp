package kekmech.ru.repository.room

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase
import android.arch.persistence.room.TypeConverters
import kekmech.ru.core.dto.CoupleNative
import kekmech.ru.core.dto.ScheduleNative
import kekmech.ru.core.dto.User

@Database(entities = [User::class, ScheduleNative::class, CoupleNative::class], version = 1)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun scheduleDao(): ScheduleDao
    abstract fun coupleDao(): CoupleDao
}