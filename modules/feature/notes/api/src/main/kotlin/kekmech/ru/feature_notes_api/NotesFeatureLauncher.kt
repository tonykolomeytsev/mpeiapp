package kekmech.ru.feature_notes_api

import kekmech.ru.domain_schedule_models.dto.Classes
import kekmech.ru.feature_notes_api.domain.model.Note
import java.time.LocalDate

interface NotesFeatureLauncher {

    fun launchNoteList(selectedClasses: Classes, selectedDate: LocalDate, resultKey: String)

    fun launchNoteEdit(note: Note, resultKey: String)

    fun launchAllNotes(selectedNote: Note? = null)
}
