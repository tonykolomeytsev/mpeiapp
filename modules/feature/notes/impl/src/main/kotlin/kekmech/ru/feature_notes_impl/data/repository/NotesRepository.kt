package kekmech.ru.feature_notes_impl.data.repository

import kekmech.ru.feature_notes_api.domain.model.Note
import kekmech.ru.feature_notes_impl.data.database.mapper.toDomain
import kekmech.ru.feature_notes_impl.data.database.mapper.toNormal
import kekmech.ru.feature_schedule_api.domain.model.SelectedSchedule
import kekmech.ru.library_app_database.dao.NoteDao

internal class NotesRepository(private val noteDao: NoteDao) {

    suspend fun getNotesBySchedule(selectedSchedule: SelectedSchedule): List<Note> =
        noteDao
            .getAllNotesForSchedule(selectedSchedule.name)
            .map { it.toDomain() }

    suspend fun updateOrInsertNoteBySchedule(selectedSchedule: SelectedSchedule, note: Note) =
        noteDao.updateOrInsert(note.toNormal(scheduleName = selectedSchedule.name))

    suspend fun deleteNote(note: Note) =
        noteDao.delete(note.toNormal(scheduleName = ""))
}
