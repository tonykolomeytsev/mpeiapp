package kekmech.ru.domain_notes.dto

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.time.LocalDateTime

@Parcelize
data class Note(
    val content: String,
    val dateTime: LocalDateTime,
    val classesName: String,
    val id: Int = -1,
    val targetRect: Int? = null, // для уточнения целевой пары при подряд идущих одинаковых парах в расписании
    val attachedPictureUrls: List<String> = emptyList()
) : Parcelable