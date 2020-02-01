package kekmech.ru.core.usecases

import kekmech.ru.core.dto.NoteNative
import kekmech.ru.core.dto.Time

interface GetNoteByTimestampUseCase {
    operator fun invoke(scheduleId: Int, timestamp: String): NoteNative?
}