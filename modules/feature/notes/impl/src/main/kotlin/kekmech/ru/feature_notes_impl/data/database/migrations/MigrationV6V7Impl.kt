package kekmech.ru.feature_notes_impl.data.database.migrations

import androidx.sqlite.db.SupportSQLiteDatabase
import kekmech.ru.lib_app_database.api.MigrationV6V7

internal class MigrationV6V7Impl : MigrationV6V7 {

    @Suppress("MaxLineLength")
    override fun migrate(database: SupportSQLiteDatabase) {
        val oldTableName = "notes"
        val newTableName = "note"
        // create new table
        database.execSQL("CREATE TABLE IF NOT EXISTS `$newTableName` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `content` TEXT NOT NULL, `timestamp` TEXT NOT NULL, `classesName` TEXT NOT NULL, `target` INTEGER NOT NULL, `associatedScheduleName` TEXT NOT NULL);")
        // copy content from old to new table, renaming columns in the SELECT,
        // because `ALTER TABLE ... RENAME COLUMN` requires SQLite 3.25+ (API 30+)
        database.execSQL("INSERT INTO `$newTableName` (`content`, `timestamp`, `classesName`, `target`, `associatedScheduleName`) SELECT COALESCE(`content`, ''), COALESCE(`datetime`, ''), COALESCE(`cls_name`, ''), 0, COALESCE(`grp_name`, '') FROM `$oldTableName`;")
        // drop old table
        database.execSQL("DROP TABLE IF EXISTS `$oldTableName`;")
    }
}
