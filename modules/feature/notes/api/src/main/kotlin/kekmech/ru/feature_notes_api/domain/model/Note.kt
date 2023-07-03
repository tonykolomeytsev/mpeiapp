package kekmech.ru.feature_notes_api.domain.model

import java.io.Serializable
import java.time.LocalDateTime

data class Note(
    val content: String,
    val dateTime: LocalDateTime,
    val classesName: String,
    val target: Int, // для уточнения целевой пары при подряд идущих одинаковых парах в расписании
    val id: Long? = null,
) : Serializable
