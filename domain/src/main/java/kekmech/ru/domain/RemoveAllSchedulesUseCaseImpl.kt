package kekmech.ru.domain

import kekmech.ru.core.repositories.NotesRepository
import kekmech.ru.core.repositories.ScheduleRepository
import kekmech.ru.core.usecases.RemoveAllSchedulesUseCase

class RemoveAllSchedulesUseCaseImpl(
    private val scheduleRepository: ScheduleRepository,
    private val notesRepository: NotesRepository
) : RemoveAllSchedulesUseCase {
    override suspend operator fun invoke() {
        try {
            notesRepository.getAll().forEach(notesRepository::removeNote)
            scheduleRepository.removeAllSchedules()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}