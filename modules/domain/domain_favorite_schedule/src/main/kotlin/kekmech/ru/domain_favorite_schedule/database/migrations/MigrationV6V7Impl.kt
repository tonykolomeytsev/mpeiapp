package kekmech.ru.domain_favorite_schedule.database.migrations

import androidx.sqlite.db.SupportSQLiteDatabase
import kekmech.ru.common_app_database_migrations.MigrationV6V7

internal class MigrationV6V7Impl : MigrationV6V7 {

    @Suppress("MaxLineLength")
    override fun migrate(database: SupportSQLiteDatabase) {
        val oldTableName = "favorite_schedules"
        val newTableName = "favorite_schedule"
        // create new table
        database.execSQL("CREATE TABLE IF NOT EXISTS `$newTableName` (`name` TEXT NOT NULL, `type` TEXT NOT NULL, `description` TEXT NOT NULL, `order` INTEGER NOT NULL, PRIMARY KEY(`name`));")
        // drop old table
        database.execSQL("DROP TABLE `$oldTableName`;")
    }
}
