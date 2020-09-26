package kekmech.ru.repository.room

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import kekmech.ru.core.dto.CoupleNative
import kekmech.ru.core.dto.NoteNative
import kekmech.ru.core.dto.ScheduleNative
import kekmech.ru.core.dto.User

@Database(entities = [NoteNative::class], version = 3, exportSchema = false)
// @TypeConverters()
abstract class AppDatabase : RoomDatabase() {
    abstract fun noteDao(): NoteDao

    companion object {
        val MIGRATION_V1_TO_V2 = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("""
                    CREATE TABLE `notes`(
                        `id` INTEGER PRIMARY KEY NOT NULL,
                        `couple_id` INTEGER NOT NULL, 
                        `tag` TEXT NOT NULL, 
                        `data` TEXT NOT NULL, 
                        `timestamp` TEXT NOT NULL
                    );
                    """)
                database.execSQL("""
                    CREATE UNIQUE INDEX index_notes_id ON notes (id)
                """)
            }
        }
        val MIGRATION_V2_TO_V3 = object : Migration(2, 3) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("DROP TABLE users;")
                database.execSQL("DROP TABLE schedules;")
                database.execSQL("DROP TABLE couples;")
                database.execSQL("DROP TABLE notes;")
            }
        }
    }
}