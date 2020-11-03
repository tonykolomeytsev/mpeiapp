package kekmech.ru.mpeiapp

import kekmech.ru.common_mvi.util.DisposableDelegate
import kekmech.ru.common_mvi.util.DisposableDelegateImpl
import kekmech.ru.domain_schedule.ScheduleRepository

class Prefetcher(
    private val scheduleRepository: ScheduleRepository
) : DisposableDelegate by DisposableDelegateImpl() {

    fun prefetch() {
        clearDisposables()
        scheduleRepository.loadSchedule()
    }
}