package kekmech.ru.common_app_database

import kekmech.ru.common_app_database.dto.Record
import kekmech.ru.common_app_database.helpers.DBHelper
import org.intellij.lang.annotations.Language

class AppDatabase(
    private val dbHelper: DBHelper
) {

    fun fetch(@Language("RoomSql") request: String): List<Record> {
        val cursor = dbHelper.writableDatabase.rawQuery(request, emptyArray())
        if (cursor.count == 0) {
            cursor.close()
            return emptyList()
        }
        val columns = cursor.columnNames
        val records = mutableListOf<Record>()
        var eof = cursor.moveToFirst()
        while (eof) {
            val row = columns.map { it to cursor[it] }.toMap()
            records += Record(row)
            eof = cursor.moveToNext()
        }
        cursor.close()
        return records
    }

    fun close() = dbHelper.close()

    companion object {
        const val VERSION = 2
        const val NAME = "mpeix.db"
    }
}