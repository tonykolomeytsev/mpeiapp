package kekmech.ru.common_app_database

import android.database.Cursor

internal operator fun Cursor.get(columnName: String): Any? {
    val columnIndex = getColumnIndexOrThrow(columnName)
    return when (getType(columnIndex)) {
        Cursor.FIELD_TYPE_INTEGER -> getInt(columnIndex)
        Cursor.FIELD_TYPE_FLOAT -> getFloat(columnIndex)
        Cursor.FIELD_TYPE_STRING -> getString(columnIndex)
        Cursor.FIELD_TYPE_BLOB -> getBlob(columnIndex)
        else -> null
    }
}