package kekmech.ru.feature_notes_api.domain.usecase

import kekmech.ru.feature_notes_api.domain.model.Note

public interface GetNotesForSelectedScheduleUseCase {

    public suspend operator fun invoke(): List<Note>
}
