package kekmech.ru.common_app_database.migrations

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import kekmech.ru.common_app_database_api.PartialMigration

internal open class BaseMigration<T : PartialMigration>(
    private val migrations: List<T>,
    fromVersion: Int,
    toVersion: Int,
) : Migration(fromVersion, toVersion) {

    override fun migrate(database: SupportSQLiteDatabase) {
        database.beginTransaction()
        migrations.forEach { it.migrate(database) }
        database.setTransactionSuccessful()
        database.endTransaction()
    }
}
