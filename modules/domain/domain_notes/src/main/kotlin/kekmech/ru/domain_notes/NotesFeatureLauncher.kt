package kekmech.ru.domain_notes

import kekmech.ru.domain_notes.dto.Note
import kekmech.ru.domain_schedule_models.dto.Classes
import java.time.LocalDate

public interface NotesFeatureLauncher {

    public fun launchNoteList(selectedClasses: Classes, selectedDate: LocalDate, resultKey: String)

    public fun launchNoteEdit(note: Note, resultKey: String)

    public fun launchAllNotes(selectedNote: Note? = null)
}
