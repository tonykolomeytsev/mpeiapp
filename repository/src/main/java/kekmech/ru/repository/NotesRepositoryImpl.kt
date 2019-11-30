package kekmech.ru.repository

import android.util.Log
import kekmech.ru.core.dto.CoupleNative
import kekmech.ru.core.dto.NoteNative
import kekmech.ru.core.dto.Time
import kekmech.ru.core.repositories.NotesRepository
import kekmech.ru.repository.room.AppDatabase
import javax.inject.Inject

class NotesRepositoryImpl @Inject constructor(
    private val appdb: AppDatabase
): NotesRepository {

    val notes: MutableList<NoteNative> by lazy { appdb.noteDao().getAll().toMutableList() }

    override var noteCreationTransaction: CoupleNative? = null

    override fun getNoteFor(time: Time): NoteNative? {
        return notes.firstOrNull { it.timestamp == time.timestamp() }
    }

    override fun removeNote(note: NoteNative) {
        if (notes.containsById(note)) {
            appdb.noteDao().delete(note)
        }
        notes.remove(note)
        Log.d("NotesRepository", "note removes: note=$note\nnotes=$notes")
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
            notes.clear()
            notes.addAll(appdb.noteDao().getAll())
            Log.d("NotesRepository", "note saved: notes=$notes")
        }
    }

    override fun getNoteFor(scheduleId: Int, dayOfWeek: Int, weekNum: Int, coupleNum: Int): NoteNative? {
        val necessaryTimestamp = "w${weekNum}d${dayOfWeek}p${coupleNum};"
        return notes
            .find { (it.timestamp == necessaryTimestamp) and (it.scheduleId == scheduleId) }
    }

    override fun getNoteDyId(id: Int): NoteNative? {
        return appdb.noteDao().getById(id)
    }

    private fun List<NoteNative>.containsById(note: NoteNative) = this.any { it.id == note.id }

    private fun Time.timestamp(): String = this.formattedAsYearMonthDay
}