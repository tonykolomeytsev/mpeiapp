package kekmech.ru.mpeiapp

import kekmech.ru.common_elm.DisposableDelegate
import kekmech.ru.common_elm.DisposableDelegateImpl
import kekmech.ru.domain_schedule.use_cases.GetCurrentScheduleUseCase

class Prefetcher(
    private val getCurrentScheduleUseCase: GetCurrentScheduleUseCase,
) : DisposableDelegate by DisposableDelegateImpl() {

    fun prefetch() {
        clearDisposables()
        getCurrentScheduleUseCase
            .getSchedule(weekOffset = 0)
            .subscribe()
            .bind()
    }
}
