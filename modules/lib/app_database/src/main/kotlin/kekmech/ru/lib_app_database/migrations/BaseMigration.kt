package kekmech.ru.lib_app_database.migrations

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.google.firebase.crashlytics.recordException
import kekmech.ru.lib_app_database.api.PartialMigration

/**
 * Aggregator of [PartialMigration]s from domain modules
 *
 * @see PartialMigration
 */
internal open class BaseMigration<T : PartialMigration>(
    private val migrations: List<T>,
    fromVersion: Int,
    toVersion: Int,
) : Migration(fromVersion, toVersion) {

    // Room already runs migrations inside a transaction, so no nested one is needed here
    override fun migrate(db: SupportSQLiteDatabase) {
        migrations.forEach { migration ->
            try {
                migration.migrate(db)
            } catch (e: Exception) {
                // a failed migration leaves the database on the old version,
                // so report the root cause before Room fails to open it
                FirebaseCrashlytics.getInstance().recordException(e) {
                    key("db_migration", "$startVersion->$endVersion")
                    key("db_partial_migration", migration::class.java.name)
                }
                throw e
            }
        }
    }
}
