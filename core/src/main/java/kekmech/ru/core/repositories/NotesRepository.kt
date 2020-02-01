package kekmech.ru.core.repositories

import kekmech.ru.core.dto.CoupleNative
import kekmech.ru.core.dto.NoteNative
import kekmech.ru.core.dto.NoteTransaction
import kekmech.ru.core.dto.Time

interface NotesRepository {
    var noteCreationTransaction: NoteTransaction?

    fun getNoteFor(scheduleId: Int, timestamp: String): NoteNative?
    fun saveNote(note: NoteNative, isNoteEmpty: Boolean = false)
    fun removeNote(note: NoteNative)
    fun getAll(): List<NoteNative>
    fun deleteAll()
}