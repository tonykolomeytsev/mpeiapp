package kekmech.ru.domain

import androidx.lifecycle.LiveData
import kekmech.ru.core.repositories.ScheduleRepository
import kekmech.ru.core.usecases.GetGroupNumberUseCase
import javax.inject.Inject

class GetGroupNumberUseCaseImpl @Inject constructor(
    private val scheduleRepository: ScheduleRepository
) : GetGroupNumberUseCase {
    override fun invoke(): LiveData<String> {
        return scheduleRepository.getGroupNum()
    }
}