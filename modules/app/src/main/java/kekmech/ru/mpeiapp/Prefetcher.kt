package kekmech.ru.mpeiapp

import kekmech.ru.common_elm.DisposableDelegate
import kekmech.ru.common_elm.DisposableDelegateImpl
import kekmech.ru.domain_schedule.ScheduleRepository

class Prefetcher(
    private val scheduleRepository: ScheduleRepository
) : DisposableDelegate by DisposableDelegateImpl() {

    fun prefetch() {
        clearDisposables()
        scheduleRepository.loadSchedule()
    }
}
