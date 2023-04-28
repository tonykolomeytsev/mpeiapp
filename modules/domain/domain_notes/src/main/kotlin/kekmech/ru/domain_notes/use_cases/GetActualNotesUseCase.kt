package kekmech.ru.domain_notes.use_cases

import io.reactivex.rxjava3.core.Single
import kekmech.ru.common_kotlin.moscowLocalDate
import kekmech.ru.domain_notes.dto.Note
import java.time.temporal.ChronoUnit

@Suppress("MagicNumber")
private val ActualNotesInterval = 0..7
private const val ActualNotesNumber = 5

class GetActualNotesUseCase(
    private val getNotesUseCase: GetNotesForSelectedScheduleUseCase,
) {

    fun getActualNotes(): Single<List<Note>> {
        val today = moscowLocalDate()
        return getNotesUseCase.getNotes()
            .map { notes ->
                notes
                    .filter { note ->
                        val noteDate = note.dateTime.toLocalDate()
                        ChronoUnit.DAYS.between(today, noteDate) in ActualNotesInterval
                    }
                    .sortedBy { it.dateTime }
                    .take(ActualNotesNumber)
            }
    }
}
