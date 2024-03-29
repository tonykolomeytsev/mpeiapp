package kekmech.ru.feature_favorite_schedule_impl.data.database.migrations

import androidx.sqlite.db.SupportSQLiteDatabase
import kekmech.ru.lib_app_database.api.MigrationV6V7

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
