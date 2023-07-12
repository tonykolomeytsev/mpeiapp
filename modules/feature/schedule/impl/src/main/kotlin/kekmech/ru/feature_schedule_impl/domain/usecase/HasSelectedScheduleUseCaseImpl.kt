package kekmech.ru.feature_schedule_impl.domain.usecase

import kekmech.ru.feature_schedule_api.data.repository.ScheduleRepository
import kekmech.ru.feature_schedule_api.domain.usecase.HasSelectedScheduleUseCase

internal class HasSelectedScheduleUseCaseImpl(
    private val scheduleRepository: ScheduleRepository,
) : HasSelectedScheduleUseCase {

    override fun invoke(): Boolean =
        runCatching { scheduleRepository.getSelectedSchedule() }.isSuccess
}
