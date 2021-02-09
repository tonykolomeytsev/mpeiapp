package kekmech.ru.common_app_database.helpers

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import kekmech.ru.common_android.getRawText
import kekmech.ru.common_app_database.R
import kekmech.ru.common_app_database.dto.Migration
import kekmech.ru.common_kotlin.fastLazy

internal class DBHelper(
    private val context: Context,
    databaseName: String,
    databaseVersion: Int
) : SQLiteOpenHelper(context, databaseName, null, databaseVersion) {

    private val dbCreateScript by fastLazy { context.getRawText(R.raw.db_create) }

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(dbCreateScript)
        setOfMigrations
            .sortedBy { it.oldVersion }
            .map { context.getRawText(it.migrationScript) }
            .forEach { db.execSQL(it) }
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        val migration = setOfMigrations
            .find { it.oldVersion == oldVersion && it.newVersion == newVersion }
            ?: error("Can't find migration for database (v$oldVersion -> v$newVersion)")
        db.execSQL(context.getRawText(migration.migrationScript))
    }

    companion object {
        val setOfMigrations by fastLazy { setOf<Migration>(
            Migration(1, 2, R.raw.migration_v1_to_v2),
            Migration(2, 3, R.raw.migration_v2_to_v3),
            Migration(3, 4, R.raw.migration_v3_to_v4)
        ) }
    }
}