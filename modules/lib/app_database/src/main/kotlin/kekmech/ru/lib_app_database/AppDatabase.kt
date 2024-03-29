package kekmech.ru.lib_app_database

import androidx.room.Database
import androidx.room.RoomDatabase
import kekmech.ru.lib_app_database.dao.FavoriteScheduleDao
import kekmech.ru.lib_app_database.dao.NoteDao
import kekmech.ru.lib_app_database.entity.NormalFavoriteSchedule
import kekmech.ru.lib_app_database.entity.NormalNote

/**
 * # Whole app database
 *
 * The database depends on the entities of the domain modules, and allows different domain modules:
 * - to declare their own Data Acces Objects (DAOs) using standard Room mechanisms,
 * - to describe migrations only for those relationships that are encapsulated inside the
 * domain modules.
 *
 * And all this despite the fact that domain modules do not know about the existence of the
 * database. The database is simply the root component for code generation.
 *
 * **Invariants:**
 * - No module other than the `app` module should depend on the `common_app_database` module.
 * - The `common_app_database` module depends on all modules that have an entity for the database.
 *
 * *Note: All modules with entities must have Kotlin Symbol Processing (KSP) enabled.*
 *
 * @see kekmech.ru.library_app_database_api.PartialMigration
 */
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
