package kekmech.ru.domain_notes.services

import kekmech.ru.domain_notes.dto.Note
import kekmech.ru.domain_notes.use_cases.GetNotesForSelectedScheduleUseCase
import kekmech.ru.domain_schedule_models.dto.Classes
import kekmech.ru.domain_schedule_models.dto.Schedule
import java.time.LocalDate
import java.time.LocalDateTime

class AttachNotesToScheduleService(
    private val getNotesUseCase: GetNotesForSelectedScheduleUseCase,
) {

    suspend fun attach(schedule: Schedule): Schedule = getNotesUseCase.invoke()
        .let { notes ->
            schedule.weeks.first().days.forEach { day ->
                day.classes.forEach { classes ->
                    val firstAttachedNote = classes.relevantToThe(notes, day.date)
                    if (firstAttachedNote != null) {
                        classes.attachedNotePreview =
                            firstAttachedNote.content.replaceNewLinesWithSpaces()
                    }
                }
            }
            schedule
        }

    private fun Classes.relevantToThe(listOfNotes: List<Note>, date: LocalDate) =
        listOfNotes.firstOrNull {
            it.classesName == name && it.dateTime == LocalDateTime.of(date, time.start)
        }

    private fun String.replaceNewLinesWithSpaces() = replace('\n', ' ')

}
