package kekmech.ru.domain_notes

import kekmech.ru.domain_notes.dto.Note
import kekmech.ru.domain_schedule.ScheduleTransformer
import kekmech.ru.domain_schedule.dto.Classes
import kekmech.ru.domain_schedule.dto.Schedule
import java.time.LocalDate
import java.time.LocalDateTime

class NotesScheduleTransformer(
    private val notesRepository: NotesRepository
) : ScheduleTransformer {

    override fun transform(schedule: Schedule) = notesRepository.getNotes()
        .map { notes ->
            schedule.weeks.first().days.forEach { day ->
                day.classes.forEach { classes ->
                    classes.hasAttachedNote = classes.relevantToThe(notes, day.date)
                }
            }
            schedule
        }

    private fun Classes.relevantToThe(listOfNotes: List<Note>, date: LocalDate) =
        listOfNotes.any { it.classesName == name && it.dateTime == LocalDateTime.of(date, time.start) }

}