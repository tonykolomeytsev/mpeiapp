package kekmech.ru.domain_notes.dto

import kekmech.ru.common_app_database_api.DefaultId
import java.io.Serializable
import java.time.LocalDateTime

data class Note(
    val content: String,
    val dateTime: LocalDateTime,
    val classesName: String,
    val target: Int, // для уточнения целевой пары при подряд идущих одинаковых парах в расписании
    val id: Long = DefaultId,
) : Serializable
