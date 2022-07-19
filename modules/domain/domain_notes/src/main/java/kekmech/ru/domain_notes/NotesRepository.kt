package kekmech.ru.domain_notes

import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.internal.operators.completable.CompletableFromRunnable
import kekmech.ru.domain_notes.dto.Note
import kekmech.ru.domain_notes.sources.NotesSource
import kekmech.ru.domain_schedule.ScheduleRepository

class NotesRepository(
    private val notesSource: NotesSource,
    private val scheduleRepository: ScheduleRepository
) {

    fun getNotes(): Single<List<Note>> = scheduleRepository
        .getSelectedScheduleName()
        .map { notesSource.getAll(it) }

    fun putNote(note: Note): Completable = scheduleRepository
        .getSelectedScheduleName()
        .map { notesSource.put(it, note) }
        .ignoreElement()

    fun deleteNote(note: Note): Completable = CompletableFromRunnable.fromRunnable {
        notesSource.delete(note)
    }
}