package kekmech.ru.domain_notes.database.migrations

import androidx.sqlite.db.SupportSQLiteDatabase
import kekmech.ru.common_app_database_api.MigrationV6V7

internal class MigrationV6V7Impl : MigrationV6V7 {

    override fun migrate(database: SupportSQLiteDatabase) {
        val newTableName = "note"
        val oldTableName = "notes"
        // rename columns
        database.execSQL("ALTER TABLE `$oldTableName` RENAME COLUMN datetime TO `timestamp`;")
        database.execSQL("ALTER TABLE `$oldTableName` RENAME COLUMN cls_name TO `classesName`;")
        database.execSQL("ALTER TABLE `$oldTableName` RENAME COLUMN grp_name TO `associatedScheduleName`;")
        // create not null column
        database.execSQL("ALTER TABLE `$oldTableName` ADD COLUMN `notNullTarget` INTEGER NOT NULL DEFAULT 0;")
        // create new table
        database.execSQL("CREATE TABLE IF NOT EXISTS `$newTableName` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `content` TEXT NOT NULL, `timestamp` TEXT NOT NULL, `classesName` TEXT NOT NULL, `target` INTEGER NOT NULL, `associatedScheduleName` TEXT NOT NULL);")
        // copy content from old to new table
        database.execSQL("INSERT INTO `$newTableName` (`content`, `timestamp`, `classesName`, `target`, `associatedScheduleName`) SELECT `content`, `timestamp`, `classesName`, `notNullTarget`, `associatedScheduleName` FROM `$oldTableName`;")
        // drop old table
        database.execSQL("DROP TABLE `$oldTableName`;")
    }
}
