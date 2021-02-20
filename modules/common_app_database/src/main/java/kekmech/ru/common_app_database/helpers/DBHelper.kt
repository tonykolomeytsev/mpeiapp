package kekmech.ru.common_app_database.helpers

import android.content.Context
import android.database.Cursor
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
        db.beginTransaction()
        try {
            db.execSQL(dbCreateScript)
            setOfMigrations
                .sortedBy { it.oldVersion }
                .map { context.getRawText(it.migrationScript) }
                .forEach { db.execSQL(it) }
            db.setTransactionSuccessful()
        } finally {
            db.endTransaction()
        }
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.beginTransaction()
        try {
            fuckingMigrationFromV150(db)
            db.setTransactionSuccessful()
        } finally {
            db.endTransaction()
        }
    }

    private fun fuckingMigrationFromV150(db: SQLiteDatabase) {
        var cur: Cursor? = null
        val fields = mutableListOf<String>()
        try {
            cur = db.rawQuery("select * from notes limit 1;", null)
            fields.addAll(cur!!.columnNames)
        } finally {
            cur?.close()
        }
        db.execSQL(context.getRawText(R.raw.migration_v1_to_v2))
        if ("p_attachments" !in fields) {
            db.execSQL(context.getRawText(R.raw.migration_v2_to_v3))
        }
        if ("target" !in fields) {
            db.execSQL(context.getRawText(R.raw.migration_v3_to_v4))
        }
    }

    companion object {
        val setOfMigrations by fastLazy { setOf<Migration>(
            /*Migration(1, 2, R.raw.migration_v1_to_v2),
            Migration(2, 3, R.raw.migration_v2_to_v3),
            Migration(3, 4, R.raw.migration_v3_to_v4)*/
        ) }
    }
}