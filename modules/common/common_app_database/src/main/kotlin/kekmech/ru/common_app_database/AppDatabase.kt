package kekmech.ru.common_app_database

import androidx.room.Database
import androidx.room.RoomDatabase
import kekmech.ru.domain_favorite_schedule.database.FavoriteScheduleDao
import kekmech.ru.domain_favorite_schedule.database.entities.NormalFavoriteSchedule
import kekmech.ru.domain_notes.database.NoteDao
import kekmech.ru.domain_notes.database.entities.NormalNote

@Database(
    entities = [
        NormalFavoriteSchedule::class,
        NormalNote::class,
    ],
    version = AppDatabase.Version,
    exportSchema = true,
    autoMigrations = [],
)
internal abstract class AppDatabase : RoomDatabase() {

    abstract fun favoriteScheduleDao(): FavoriteScheduleDao

    abstract fun noteDao(): NoteDao

    companion object {

        const val Version = 7
        const val Name = "mpeix.db"
    }
}
