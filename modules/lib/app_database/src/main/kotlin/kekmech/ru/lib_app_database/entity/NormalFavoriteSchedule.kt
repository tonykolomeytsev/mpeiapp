package kekmech.ru.lib_app_database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorite_schedule")
data class NormalFavoriteSchedule(
    @PrimaryKey
    val name: String,
    val type: String,
    val description: String,
    val order: Int,
)
