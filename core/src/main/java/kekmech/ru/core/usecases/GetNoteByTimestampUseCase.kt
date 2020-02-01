package kekmech.ru.core.usecases

import kekmech.ru.core.dto.NoteNative
import kekmech.ru.core.dto.Time

interface GetNoteByTimestampUseCase {
    operator fun invoke(timestamp: String): NoteNative?
}