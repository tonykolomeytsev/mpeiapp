package kekmech.ru.domain

import androidx.lifecycle.LiveData
import kekmech.ru.core.repositories.OldScheduleRepository
import kekmech.ru.core.usecases.IsNeedToUpdateFeedUseCase

class IsNeedToUpdateFeedUseCaseImpl constructor(
    private val scheduleRepository: OldScheduleRepository
) : IsNeedToUpdateFeedUseCase {
    override fun invoke(): LiveData<Boolean> {
        return scheduleRepository.isNeedToUpdateFeed
    }
}