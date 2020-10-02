package kekmech.ru.domain_notes

import kekmech.ru.domain_notes.dto.Note

interface NotesSource {
    fun getAll(groupName: String): List<Note>
    fun put(groupName: String, note: Note)
    fun delete(note: Note)
}