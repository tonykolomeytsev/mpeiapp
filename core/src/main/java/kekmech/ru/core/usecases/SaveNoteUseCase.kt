package kekmech.ru.core.usecases

import kekmech.ru.core.dto.NoteNative

interface SaveNoteUseCase {
    operator fun invoke(note: NoteNative)
}