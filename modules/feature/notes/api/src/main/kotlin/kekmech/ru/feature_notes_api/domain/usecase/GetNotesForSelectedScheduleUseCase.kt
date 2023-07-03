package kekmech.ru.feature_notes_api.domain.usecase

import kekmech.ru.feature_notes_api.domain.model.Note

interface GetNotesForSelectedScheduleUseCase {

    suspend operator fun invoke(): List<Note>
}
