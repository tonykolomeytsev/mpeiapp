package kekmech.ru.domain

import kekmech.ru.core.dto.NoteNative
import kekmech.ru.core.repositories.NotesRepository
import kekmech.ru.core.usecases.GetNoteByIdUseCase
import javax.inject.Inject

class GetNoteByIdUseCaseImpl @Inject constructor(
    private val notesRepository: NotesRepository
): GetNoteByIdUseCase {
    override fun invoke(id: Int): NoteNative? {
        return notesRepository.getNoteDyId(id)
    }
}