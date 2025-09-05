package kekmech.ru.domain_notes.dto

import kekmech.ru.common_app_database_api.DefaultId
import java.io.Serializable
import java.time.LocalDateTime

public data class Note(
    val content: String,
    val dateTime: LocalDateTime,
    val classesName: String,
    val target: Int, // для уточнения целевой пары при подряд идущих одинаковых парах в расписании
) : Serializable {

    internal var id: Long = DefaultId

    public fun same(other: Note): Boolean = id == other.id
}
