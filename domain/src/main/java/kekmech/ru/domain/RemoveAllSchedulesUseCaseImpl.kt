package kekmech.ru.domain

import kekmech.ru.core.repositories.NotesRepository
import kekmech.ru.core.repositories.ScheduleRepository
import kekmech.ru.core.usecases.RemoveAllSchedulesUseCase
import java.lang.Exception

class RemoveAllSchedulesUseCaseImpl(
    private val scheduleRepository: ScheduleRepository,
    private val notesRepository: NotesRepository
) : RemoveAllSchedulesUseCase {
    override fun invoke() {
        try {
            notesRepository.getAll().forEach(notesRepository::removeNote)
            scheduleRepository.getAllSchedules().forEach(scheduleRepository::removeSchedule)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}