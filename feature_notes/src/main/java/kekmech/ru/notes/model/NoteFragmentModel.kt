package kekmech.ru.notes.model

import kekmech.ru.core.dto.CoupleNative
import kekmech.ru.core.dto.NoteNative

interface NoteFragmentModel {
    fun getNoteContent(): String
    fun saveNote(note: NoteNative.Note)

    val transactedRealWeek: Int?
    val transactedCouple: CoupleNative?
}