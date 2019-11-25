package kekmech.ru.core.usecases

import kekmech.ru.core.dto.NoteNative

interface RemoveNoteUseCase {
    operator fun invoke(note: NoteNative)
}