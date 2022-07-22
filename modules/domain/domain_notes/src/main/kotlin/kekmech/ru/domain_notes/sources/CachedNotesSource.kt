package kekmech.ru.domain_notes.sources

import kekmech.ru.domain_notes.dto.Note

class CachedNotesSource(
    private val notesSource: NotesSource
) : NotesSource {

    private var lastGroupName = ""
    private val cache = mutableListOf<Note>()

    override fun getAll(groupName: String): List<Note> {
        if (lastGroupName != groupName) {
            invalidateCacheWithNew(groupName)
        }
        return cache
    }

    override fun put(groupName: String, note: Note) {
        notesSource.put(groupName, note)
        invalidateCacheWithNew(groupName)
    }

    override fun delete(note: Note) {
        notesSource.delete(note)
        cache.remove(note)
    }

    private fun invalidateCacheWithNew(groupName: String) {
        lastGroupName = groupName
        cache.clear()
        cache.addAll(notesSource.getAll(groupName))
    }
}
