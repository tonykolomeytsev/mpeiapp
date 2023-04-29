package kekmech.ru.common_app_database_api

import androidx.sqlite.db.SupportSQLiteDatabase

interface PartialMigration {

    fun migrate(database: SupportSQLiteDatabase)
}
