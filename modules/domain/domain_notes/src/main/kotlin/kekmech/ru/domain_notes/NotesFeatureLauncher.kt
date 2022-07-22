package kekmech.ru.domain_notes

import kekmech.ru.domain_notes.dto.Note
import kekmech.ru.domain_schedule.dto.Classes
import java.time.LocalDate

interface NotesFeatureLauncher {

    fun launchNoteList(selectedClasses: Classes, selectedDate: LocalDate, resultKey: String)

    fun launchNoteEdit(note: Note, resultKey: String)

    fun launchAllNotes(selectedNote: Note? = null)
}
