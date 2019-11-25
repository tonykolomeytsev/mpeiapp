package kekmech.ru.repository

import kekmech.ru.core.dto.NoteNative
import kekmech.ru.core.dto.Time
import kekmech.ru.core.repositories.NotesRepository
import kekmech.ru.repository.room.AppDatabase
import javax.inject.Inject

class NotesRepositoryImpl @Inject constructor(
    private val appdb: AppDatabase
): NotesRepository {

    val notes: MutableList<NoteNative> by lazy { appdb.noteDao().getAll().toMutableList() }

    override fun getNoteFor(time: Time): NoteNative? {
        return notes.firstOrNull { it.timestamp == time.timestamp() }
    }

    override fun removeNote(note: NoteNative) {
        if (notes.containsById(note)) {
            appdb.noteDao().delete(note)
        }
        notes.remove(note)
    }

    override fun saveNote(note: NoteNative) {
        if (notes.containsById(note)) {
            appdb.noteDao().update(note)
        } else {
            appdb.noteDao().insert(note)
        }
        notes.clear()
        notes.addAll(appdb.noteDao().getAll())
    }

    private fun List<NoteNative>.containsById(note: NoteNative) = this.any { it.id == note.id }

    private fun Time.timestamp(): String = this.formattedAsYearMonthDay
}