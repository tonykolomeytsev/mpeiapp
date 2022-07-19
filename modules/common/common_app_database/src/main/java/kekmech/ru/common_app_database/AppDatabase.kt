package kekmech.ru.common_app_database

import kekmech.ru.common_app_database.dto.Record
import org.intellij.lang.annotations.Language

interface AppDatabase {
    fun fetch(@Language("RoomSql") request: String): List<Record>
    fun close()

    companion object {
        const val VERSION = 6
        const val NAME = "mpeix.db"
    }
}