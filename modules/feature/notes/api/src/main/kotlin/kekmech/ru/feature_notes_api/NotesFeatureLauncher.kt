package kekmech.ru.feature_notes_api

import kekmech.ru.feature_notes_api.domain.model.Note
import kekmech.ru.feature_schedule_api.domain.model.Classes
import java.time.LocalDate

public interface NotesFeatureLauncher {

    public fun launchNoteList(selectedClasses: Classes, selectedDate: LocalDate, resultKey: String)

    public fun launchNoteEdit(note: Note, resultKey: String)

    public fun launchAllNotes(selectedNote: Note? = null)
}
