package kekmech.ru.domain

import androidx.lifecycle.LiveData
import kekmech.ru.core.repositories.OldScheduleRepository
import kekmech.ru.core.usecases.GetGroupNumberUseCase

class GetGroupNumberUseCaseImpl constructor(
    private val scheduleRepository: OldScheduleRepository
) : GetGroupNumberUseCase {
    override fun invoke(): LiveData<String> {
        return scheduleRepository.getGroupNum()
    }
}