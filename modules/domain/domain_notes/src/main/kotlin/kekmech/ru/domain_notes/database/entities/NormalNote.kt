package kekmech.ru.domain_notes.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import kekmech.ru.common_app_database_api.DefaultId
import kekmech.ru.common_kotlin.fromBase64
import kekmech.ru.common_kotlin.toBase64
import kekmech.ru.domain_notes.dto.Note
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Entity(tableName = "note")
@TypeConverters(NormalNoteTypeConverter::class)
data class NormalNote(
    @PrimaryKey(autoGenerate = true)
    val id: Long = DefaultId,
    val content: String,
    val timestamp: LocalDateTime,
    val classesName: String,
    val target: Int,
    val associatedScheduleName: String,
)

@Suppress("unused")
internal class NormalNoteTypeConverter {

    @TypeConverter
    fun toLocalDateTime(string: String): LocalDateTime =
        LocalDateTime.parse(string, DateTimeFormatter.ISO_LOCAL_DATE_TIME)

    @TypeConverter
    fun fromLocalDateTime(dateTime: LocalDateTime): String =
        dateTime.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)
}

internal fun NormalNote.toDomain(): Note =
    Note(
        content = content.fromBase64(),
        dateTime = timestamp,
        classesName = classesName,
        target = target,
        id = id,
    )

internal fun Note.toNormal(scheduleName: String): NormalNote =
    NormalNote(
        content = content.toBase64(),
        timestamp = dateTime,
        classesName = classesName,
        target = target,
        id = id,
        associatedScheduleName = scheduleName,
    )