package kekmech.ru.domain

import androidx.lifecycle.LiveData
import kekmech.ru.core.repositories.ScheduleRepository
import kekmech.ru.core.usecases.GetGroupNumberUseCase

class GetGroupNumberUseCaseImpl constructor(
    private val scheduleRepository: ScheduleRepository
) : GetGroupNumberUseCase {
    override fun invoke(): LiveData<String> {
        return scheduleRepository.getGroupNum()
    }
}