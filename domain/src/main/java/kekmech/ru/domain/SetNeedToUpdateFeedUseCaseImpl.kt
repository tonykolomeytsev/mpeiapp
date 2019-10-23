package kekmech.ru.domain

import kekmech.ru.core.repositories.ScheduleRepository
import kekmech.ru.core.usecases.SetNeedToUpdateFeedUseCase
import javax.inject.Inject

class SetNeedToUpdateFeedUseCaseImpl @Inject constructor(
    private val scheduleRepository: ScheduleRepository
) : SetNeedToUpdateFeedUseCase {
    override fun invoke(update: Boolean) {
        scheduleRepository.isNeedToUpdateFeed.value = update
    }
}