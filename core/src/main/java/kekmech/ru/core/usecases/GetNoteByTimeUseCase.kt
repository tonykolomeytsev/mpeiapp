package kekmech.ru.core.usecases

import kekmech.ru.core.dto.NoteNative
import kekmech.ru.core.dto.Time

interface GetNoteByTimeUseCase {
    operator fun invoke(time: Time): NoteNative
}