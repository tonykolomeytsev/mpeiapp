package kekmech.ru.common_app_database.helpers

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import kekmech.ru.common_android.getRawText
import kekmech.ru.common_app_database.R
import kekmech.ru.common_app_database.dto.Migration
import kekmech.ru.common_kotlin.fastLazy

class DBHelper(
    private val context: Context,
    databaseName: String,
    databaseVersion: Int
) : SQLiteOpenHelper(context, databaseName, null, databaseVersion) {

    private val dbCreateScript by fastLazy { context.getRawText(R.raw.db_create) }

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(dbCreateScript)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        val migration = setOfMigrations
            .find { it.oldVersion == oldVersion && it.newVersion == newVersion }
            ?: error("Can't find migration for database")
        db.execSQL(context.getRawText(migration.migrationScript))
    }

    companion object {
        val setOfMigrations by fastLazy { setOf<Migration>() }
    }
}