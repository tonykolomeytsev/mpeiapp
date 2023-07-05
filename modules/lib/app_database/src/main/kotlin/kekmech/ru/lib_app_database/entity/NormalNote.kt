package kekmech.ru.lib_app_database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import kekmech.ru.lib_app_database.api.DefaultId

@Entity(tableName = "note")
data class NormalNote(
    @PrimaryKey(autoGenerate = true)
    val id: Long = DefaultId,
    val content: String,
    val timestamp: String,
    val classesName: String,
    val target: Int,
    val associatedScheduleName: String,
)
