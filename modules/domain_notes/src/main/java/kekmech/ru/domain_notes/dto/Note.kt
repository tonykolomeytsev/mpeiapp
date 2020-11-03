package kekmech.ru.domain_notes.dto

import java.io.Serializable
import java.time.LocalDateTime

data class Note(
    val content: String,
    val dateTime: LocalDateTime,
    val classesName: String,
    val id: Int = -1
) : Serializable