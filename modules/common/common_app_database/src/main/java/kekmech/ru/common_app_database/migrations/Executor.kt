package kekmech.ru.common_app_database.migrations

import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import org.intellij.lang.annotations.Language

internal interface Executor {
    fun getTablesNames(): Set<String>
    fun getColumnsNamesFor(tableName: String): Set<String>
    fun execSQL(@Language("RoomSql") sql: String)
}

internal class AndroidSQLiteExecutor(private val db: SQLiteDatabase) : Executor {

    override fun execSQL(sql: String) = db.execSQL(sql)

    override fun getTablesNames(): Set<String> {
        val existingTablesNames = mutableSetOf<String>()
        var cursor: Cursor? = null
        try {
            cursor = db.rawQuery("SELECT name FROM sqlite_master WHERE type='table';", null)
            if (cursor.moveToFirst()) {
                while (!cursor.isAfterLast) {
                    existingTablesNames += cursor.getString(0)
                    cursor.moveToNext()
                }
            }
        } finally {
            cursor?.close()
        }
        return existingTablesNames
    }

    override fun getColumnsNamesFor(tableName: String): Set<String> {
        val existingColumnsNames = mutableSetOf<String>()
        var cursor: Cursor? = null
        try {
            cursor = db.rawQuery("SELECT * FROM $tableName LIMIT 1;", null)
            existingColumnsNames.addAll(cursor.columnNames)
        } finally {
            cursor?.close()
        }
        return existingColumnsNames
    }
}
