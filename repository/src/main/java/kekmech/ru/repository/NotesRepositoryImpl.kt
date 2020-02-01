package kekmech.ru.repository

import android.util.Log
import kekmech.ru.core.dto.NoteNative
import kekmech.ru.core.dto.NoteTransaction
import kekmech.ru.core.dto.Time
import kekmech.ru.core.repositories.NotesRepository
import kekmech.ru.core.repositories.ScheduleRepository
import kekmech.ru.repository.room.AppDatabase

class NotesRepositoryImpl constructor(
    private val appdb: AppDatabase
): NotesRepository {

    private val notes get() = appdb.noteDao().getAll()

    override var noteCreationTransaction: NoteTransaction? = null

    override fun removeNote(note: NoteNative) {
        appdb.noteDao().getById(note.id)?.let { appdb.noteDao().delete(it) }
        Log.d("NotesRepository", "note removes: note=$note\nnotes=$notes")
    }

    override fun getNoteFor(scheduleId: Int, timestamp: String): NoteNative? {
        val allNotes = appdb.noteDao().getAllByScheduleId(scheduleId)
        return allNotes.firstOrNull { it.timestamp == timestamp }
    }

    override fun saveNote(note: NoteNative, isNoteEmpty: Boolean) {
        val similarNote = notes.find { it.timestamp == note.timestamp && it.scheduleId == note.scheduleId }
        if (isNoteEmpty) {
            if (similarNote != null) removeNote(similarNote)
        } else {
            if (similarNote != null) {
                note.id = similarNote.id
                appdb.noteDao().update(note)
            } else {
                appdb.noteDao().insert(note)
            }
            Log.d("NotesRepository", "note saved: $note\n$similarNote, \nnotes=$notes")
        }
    }

    private fun List<NoteNative>.containsById(note: NoteNative) = this.any { it.id == note.id }

    override fun getAll(): List<NoteNative> {
        return appdb.noteDao().getAll()
    }

    override fun deleteAll() {
        appdb.noteDao().getAll().forEach(appdb.noteDao()::delete)
    }
}