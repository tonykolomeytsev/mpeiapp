package kekmech.ru.domain

import kekmech.ru.core.repositories.OldScheduleRepository
import kekmech.ru.core.usecases.SetNeedToUpdateFeedUseCase

class SetNeedToUpdateFeedUseCaseImpl constructor(
    private val scheduleRepository: OldScheduleRepository
) : SetNeedToUpdateFeedUseCase {
    override fun invoke(update: Boolean) {
        scheduleRepository.isNeedToUpdateFeed.value = update
    }
}