package kekmech.ru.domain_notes

import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single
import kekmech.ru.domain_notes.dto.Note
import kekmech.ru.domain_notes.sources.NotesSource
import kekmech.ru.domain_schedule.dto.SelectedSchedule

class NotesRepository(private val notesSource: NotesSource) {

    fun getNotesBySchedule(selectedSchedule: SelectedSchedule): Single<List<Note>> =
        Single.fromCallable { notesSource.getAll(selectedSchedule.name) }

    fun putNoteBySchedule(selectedSchedule: SelectedSchedule, note: Note): Completable =
        Completable.fromAction { notesSource.put(selectedSchedule.name, note) }

    fun deleteNote(note: Note): Completable =
        Completable.fromAction { notesSource.delete(note) }
}
