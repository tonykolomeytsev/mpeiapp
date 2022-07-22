package kekmech.ru.common_app_database.helpers

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import kekmech.ru.common_app_database.Schema
import kekmech.ru.common_app_database.migrations.AndroidSQLiteExecutor

internal class DBHelper(
    context: Context,
    databaseName: String,
    databaseVersion: Int
) : SQLiteOpenHelper(context, databaseName, null, databaseVersion) {

    override fun onCreate(db: SQLiteDatabase) {
        db.beginTransaction()
        try {
            Schema.migrate(AndroidSQLiteExecutor(db))
            db.setTransactionSuccessful()
        } finally {
            db.endTransaction()
        }
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.beginTransaction()
        try {
            Schema.migrate(AndroidSQLiteExecutor(db))
            db.setTransactionSuccessful()
        } finally {
            db.endTransaction()
        }
    }
}
