package kekmech.ru.common_app_database.dto

import androidx.annotation.RawRes

data class Migration(
    val oldVersion: Int,
    val newVersion: Int,
    @RawRes val migrationScript: Int
)