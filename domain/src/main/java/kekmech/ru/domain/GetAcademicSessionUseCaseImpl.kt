package kekmech.ru.domain

import kekmech.ru.core.dto.AcademicSession
import kekmech.ru.core.repositories.OldScheduleRepository
import kekmech.ru.core.usecases.GetAcademicSessionUseCase

class GetAcademicSessionUseCaseImpl constructor(
    private val scheduleRepository: OldScheduleRepository
) : GetAcademicSessionUseCase {
    override fun invoke(): AcademicSession? {
        try {
            return scheduleRepository.loadSessionFromRemote()
        } catch (e: Exception) {
            return null
        }
    }
}