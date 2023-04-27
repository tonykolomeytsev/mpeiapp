package kekmech.ru.domain_dashboard.use_cases

import io.reactivex.rxjava3.core.Single
import kekmech.ru.domain_dashboard.dto.UpcomingEventsPrediction
import kekmech.ru.domain_schedule.ScheduleRepository

class GetUpcomingEventsUseCase(
    private val scheduleRepository: ScheduleRepository,
) {

    fun getPrediction(): Single<UpcomingEventsPrediction> {
        TODO()
    }
}
