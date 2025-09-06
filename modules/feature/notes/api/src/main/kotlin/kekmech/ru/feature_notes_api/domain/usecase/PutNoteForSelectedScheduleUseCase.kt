package kekmech.ru.feature_notes_api.domain.usecase

import kekmech.ru.feature_notes_api.domain.model.Note

public interface PutNoteForSelectedScheduleUseCase {

    public suspend operator fun invoke(note: Note)
}
