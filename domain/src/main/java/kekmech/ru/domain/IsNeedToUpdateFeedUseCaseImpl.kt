package kekmech.ru.domain

import androidx.lifecycle.LiveData
import kekmech.ru.core.repositories.ScheduleRepository
import kekmech.ru.core.usecases.IsNeedToUpdateFeedUseCase

class IsNeedToUpdateFeedUseCaseImpl constructor(
    private val scheduleRepository: ScheduleRepository
) : IsNeedToUpdateFeedUseCase {
    override fun invoke(): LiveData<Boolean> {
        return scheduleRepository.isNeedToUpdateFeed
    }
}