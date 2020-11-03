package kekmech.ru.common_app_database.dto

data class Record(
    val row: Map<String, Any?>
) {

    inline fun <reified T : Any> get(columnName: String): T? {
        return row[columnName]?.let { it as T }
    }
}