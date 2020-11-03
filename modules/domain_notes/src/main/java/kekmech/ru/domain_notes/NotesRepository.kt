package kekmech.ru.domain_notes

import io.reactivex.Completable
import io.reactivex.Single
import io.reactivex.internal.operators.completable.CompletableFromRunnable
import kekmech.ru.domain_notes.dto.Note
import kekmech.ru.domain_notes.sources.NotesSource
import kekmech.ru.domain_schedule.ScheduleRepository

class NotesRepository(
    private val notesSource: NotesSource,
    private val scheduleRepository: ScheduleRepository
) {

    fun getNotes(): Single<List<Note>> = scheduleRepository
        .getSelectedGroup()
        .map { notesSource.getAll(it) }

    fun putNote(note: Note): Completable = scheduleRepository
        .getSelectedGroup()
        .map { notesSource.put(it, note) }
        .ignoreElement()

    fun deleteNote(note: Note): Completable = CompletableFromRunnable.fromRunnable {
        notesSource.delete(note)
    }
}