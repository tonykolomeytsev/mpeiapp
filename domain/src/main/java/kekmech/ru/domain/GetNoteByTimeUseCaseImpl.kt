package kekmech.ru.domain

import kekmech.ru.core.dto.NoteNative
import kekmech.ru.core.dto.Time
import kekmech.ru.core.repositories.NotesRepository
import kekmech.ru.core.usecases.GetNoteByTimeUseCase
import javax.inject.Inject

class GetNoteByTimeUseCaseImpl @Inject constructor(
    private val notesRepository: NotesRepository
) : GetNoteByTimeUseCase {
    override fun invoke(time: Time): NoteNative? {
        return notesRepository.getNoteFor(time)
    }
}