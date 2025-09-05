package kekmech.ru.domain_notes.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import kekmech.ru.common_app_database_api.DefaultId
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Entity(tableName = "note")
@TypeConverters(NormalNoteTypeConverter::class)
public data class NormalNote(
    @PrimaryKey(autoGenerate = true)
    val id: Long = DefaultId,
    val content: String,
    val timestamp: LocalDateTime,
    val classesName: String,
    val target: Int,
    val associatedScheduleName: String,
)

@Suppress("unused")
public class NormalNoteTypeConverter {

    @TypeConverter
    public fun toLocalDateTime(string: String): LocalDateTime =
        LocalDateTime.parse(string, DateTimeFormatter.ISO_LOCAL_DATE_TIME)

    @TypeConverter
    public fun fromLocalDateTime(dateTime: LocalDateTime): String =
        dateTime.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)
}
