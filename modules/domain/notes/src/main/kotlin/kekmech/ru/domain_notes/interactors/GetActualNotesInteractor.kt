package kekmech.ru.domain_notes.interactors

import kekmech.ru.domain_notes.dto.Note
import kekmech.ru.domain_notes.use_cases.GetNotesForSelectedScheduleUseCase
import kekmech.ru.ext_kotlin.moscowLocalDate
import java.time.temporal.ChronoUnit

@Suppress("MagicNumber")
private val ActualNotesInterval = 0..7
private const val ActualNotesNumber = 5

class GetActualNotesInteractor(
    private val getNotesUseCase: GetNotesForSelectedScheduleUseCase,
) {

    suspend operator fun invoke(): List<Note> {
        val today = moscowLocalDate()
        return getNotesUseCase.invoke()
            .filter { note ->
                val noteDate = note.dateTime.toLocalDate()
                ChronoUnit.DAYS.between(today, noteDate) in ActualNotesInterval
            }
            .sortedBy { it.dateTime }
            .take(ActualNotesNumber)
    }
}
