package kekmech.ru.library_app_database_impl

import androidx.room.Database
import androidx.room.RoomDatabase
import kekmech.ru.domain_favorite_schedule.database.FavoriteScheduleDao
import kekmech.ru.domain_favorite_schedule.database.entities.NormalFavoriteSchedule
import kekmech.ru.domain_notes.database.NoteDao
import kekmech.ru.domain_notes.database.entities.NormalNote

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
