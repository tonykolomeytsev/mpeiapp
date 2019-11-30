package kekmech.ru.core.usecases

import kekmech.ru.core.dto.NoteNative

interface GetNoteByIdUseCase {
    operator fun invoke(id: Int): NoteNative?
}