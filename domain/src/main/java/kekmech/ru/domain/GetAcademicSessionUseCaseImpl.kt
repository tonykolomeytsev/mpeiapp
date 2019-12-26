package kekmech.ru.domain

import kekmech.ru.core.dto.AcademicSession
import kekmech.ru.core.repositories.ScheduleRepository
import kekmech.ru.core.usecases.GetAcademicSessionUseCase

class GetAcademicSessionUseCaseImpl constructor(
    private val scheduleRepository: ScheduleRepository
) : GetAcademicSessionUseCase {
    override fun invoke(): AcademicSession? {
        try {
            return scheduleRepository.loadSessionFromRemote()
        } catch (e: Exception) {
            return null
        }
    }
}