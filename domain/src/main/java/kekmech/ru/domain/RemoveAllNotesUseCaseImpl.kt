package kekmech.ru.domain

import kekmech.ru.core.repositories.NotesRepository
import kekmech.ru.core.usecases.RemoveAllNotesUseCase
import java.lang.Exception

class RemoveAllNotesUseCaseImpl(
    private val notesRepository: NotesRepository
) : RemoveAllNotesUseCase {

    override fun invoke() {
        try {
            notesRepository.getAll().forEach(notesRepository::removeNote)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}