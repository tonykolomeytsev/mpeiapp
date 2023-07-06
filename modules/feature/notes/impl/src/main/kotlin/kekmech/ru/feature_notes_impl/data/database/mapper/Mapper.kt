package kekmech.ru.feature_notes_impl.data.database.mapper

import kekmech.ru.ext_kotlin.fromBase64
import kekmech.ru.ext_kotlin.toBase64
import kekmech.ru.feature_notes_api.domain.model.Note
import kekmech.ru.lib_app_database.api.DefaultId
import kekmech.ru.lib_app_database.entity.NormalNote
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

internal fun NormalNote.toDomain(): Note =
    Note(
        content = content.fromBase64(),
        dateTime = timestamp.toDomain(),
        classesName = classesName,
        target = target,
        id = id,
    )

internal fun Note.toNormal(scheduleName: String): NormalNote =
    NormalNote(
        content = content.toBase64(),
        timestamp = dateTime.toEntity(),
        classesName = classesName,
        target = target,
        id = id ?: DefaultId,
        associatedScheduleName = scheduleName,
    )

private fun String.toDomain(): LocalDateTime =
    LocalDateTime.parse(this, DateTimeFormatter.ISO_LOCAL_DATE_TIME)

private fun LocalDateTime.toEntity(): String =
    format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)
