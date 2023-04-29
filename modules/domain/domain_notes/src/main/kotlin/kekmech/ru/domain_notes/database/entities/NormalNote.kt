package kekmech.ru.domain_notes.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "note")
data class NormalNote(
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    val content: String,
    val timestamp: String,
    val classesName: String,
    val target: Int,
    val associatedScheduleName: String,
)

