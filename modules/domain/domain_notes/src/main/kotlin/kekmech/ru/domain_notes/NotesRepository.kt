package kekmech.ru.domain_notes

import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single
import kekmech.ru.common_kotlin.fromBase64
import kekmech.ru.common_kotlin.toBase64
import kekmech.ru.domain_notes.database.NoteDao
import kekmech.ru.domain_notes.database.entities.NormalNote
import kekmech.ru.domain_notes.dto.Note
import kekmech.ru.domain_schedule.dto.SelectedSchedule
import java.time.LocalDateTime

class NotesRepository(private val noteDao: NoteDao) {

    fun getNotesBySchedule(selectedSchedule: SelectedSchedule): Single<List<Note>> =
        noteDao
            .getAllNotesForSchedule(selectedSchedule.name)
            .map { it.map(::fromNormal) }

    fun putNoteBySchedule(selectedSchedule: SelectedSchedule, note: Note): Completable =
        noteDao.updateOrInsert(
            toNormal(
                note = note,
                scheduleName = selectedSchedule.name,
            )
        )

    fun deleteNote(note: Note): Completable =
        noteDao.delete(toNormal(note))

    private fun fromNormal(normalNote: NormalNote): Note =
        Note(
            content = normalNote.content.fromBase64(),
            dateTime = LocalDateTime.parse(normalNote.timestamp),
            classesName = normalNote.classesName,
            target = normalNote.target,
            id = normalNote.id,
        )

    private fun toNormal(note: Note, scheduleName: String = ""): NormalNote =
        NormalNote(
            content = note.content.toBase64(),
            timestamp = note.dateTime.toString(),
            classesName = note.classesName,
            target = note.target,
            id = note.id,
            associatedScheduleName = scheduleName,
        )
}
