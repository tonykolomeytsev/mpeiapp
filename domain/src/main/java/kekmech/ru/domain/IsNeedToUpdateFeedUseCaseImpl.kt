package kekmech.ru.domain

import kekmech.ru.core.repositories.ScheduleRepository
import kekmech.ru.core.usecases.IsNeedToUpdateFeedUseCase
import javax.inject.Inject

class IsNeedToUpdateFeedUseCaseImpl @Inject constructor(
    private val scheduleRepository: ScheduleRepository
) : IsNeedToUpdateFeedUseCase {
    override fun invoke(): Boolean {
        return scheduleRepository.isNeedToUpdateFeed
    }
}