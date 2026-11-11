package kekmech.ru.feature_schedule_api.domain.usecase

import kekmech.ru.feature_schedule_api.data.repository.ScheduleRepository
import kekmech.ru.feature_schedule_api.domain.model.ScheduleKey
import kekmech.ru.feature_schedule_api.domain.model.ScheduleResource
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest

/**
 * Cache-first subscription to one week of the selected schedule.
 * Switches to the new schedule automatically when the user selects another one.
 *
 * @see ScheduleRepository.observeSchedule
 */
public class ObserveCurrentScheduleUseCase(
    private val scheduleRepository: ScheduleRepository,
) {

    /**
     * @param weekOffset 0 is the current week (Moscow time), 1 is the next one
     */
    @OptIn(ExperimentalCoroutinesApi::class)
    public operator fun invoke(weekOffset: Int): Flow<ScheduleResource> =
        scheduleRepository
            .observeSelectedSchedule()
            .flatMapLatest { selectedSchedule ->
                scheduleRepository.observeSchedule(ScheduleKey.of(selectedSchedule, weekOffset))
            }
}
