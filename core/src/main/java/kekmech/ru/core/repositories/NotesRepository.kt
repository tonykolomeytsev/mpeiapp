package kekmech.ru.core.repositories

import kekmech.ru.core.dto.NoteNative
import kekmech.ru.core.dto.Time

interface NotesRepository {
    fun getNoteFor(time: Time): NoteNative?
    fun saveNote(note: NoteNative)
    fun removeNote(note: NoteNative)
}