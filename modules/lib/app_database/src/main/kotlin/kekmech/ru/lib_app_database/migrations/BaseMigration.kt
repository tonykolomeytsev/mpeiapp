package kekmech.ru.lib_app_database.migrations

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
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

    override fun migrate(db: SupportSQLiteDatabase) {
        db.beginTransaction()
        migrations.forEach { it.migrate(db) }
        db.setTransactionSuccessful()
        db.endTransaction()
    }
}
