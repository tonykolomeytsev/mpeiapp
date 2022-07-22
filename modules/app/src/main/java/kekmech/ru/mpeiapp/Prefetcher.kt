package kekmech.ru.mpeiapp

import kekmech.ru.common_mvi.DisposableDelegate
import kekmech.ru.common_mvi.DisposableDelegateImpl
import kekmech.ru.domain_schedule.ScheduleRepository

class Prefetcher(
    private val scheduleRepository: ScheduleRepository
) : DisposableDelegate by DisposableDelegateImpl() {

    fun prefetch() {
        clearDisposables()
        scheduleRepository.loadSchedule()
    }
}
