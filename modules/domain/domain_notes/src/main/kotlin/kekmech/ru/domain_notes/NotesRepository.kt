package kekmech.ru.domain_notes

import kekmech.ru.domain_notes.database.NoteDao
import kekmech.ru.domain_notes.database.entities.toDomain
import kekmech.ru.domain_notes.database.entities.toNormal
import kekmech.ru.domain_notes.dto.Note
import kekmech.ru.domain_schedule.dto.SelectedSchedule

class NotesRepository(private val noteDao: NoteDao) {

    suspend fun getNotesBySchedule(selectedSchedule: SelectedSchedule): List<Note> =
        noteDao
            .getAllNotesForSchedule(selectedSchedule.name)
            .map { it.toDomain() }

    suspend fun updateOrInsertNoteBySchedule(selectedSchedule: SelectedSchedule, note: Note) =
        noteDao.updateOrInsert(note.toNormal(scheduleName = selectedSchedule.name))

    suspend fun deleteNote(note: Note) =
        noteDao.delete(note.toNormal(scheduleName = ""))
}
