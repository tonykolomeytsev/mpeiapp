package kekmech.ru.domain_schedule.data

import com.google.firebase.crashlytics.FirebaseCrashlytics
import kekmech.ru.domain_analytics.SelectedScheduleAnalyticsProvider
import kekmech.ru.domain_schedule.PreheatSelectedScheduleProvider
import kekmech.ru.domain_schedule.dto.SelectedSchedule
import kekmech.ru.domain_schedule.network.ScheduleService
import kekmech.ru.domain_schedule_models.dto.Schedule
import kekmech.ru.domain_schedule_models.dto.ScheduleType

internal class ScheduleRepositoryImpl(
    private val scheduleService: ScheduleService,
    private val scheduleCacheWrapper: ScheduleCacheWrapper,
    selectedScheduleSource: SelectedScheduleSource,
) :
    ScheduleRepository,
    SelectedScheduleAnalyticsProvider,
    PreheatSelectedScheduleProvider {

    private var _selectedSchedule: SelectedSchedule by selectedScheduleSource

    override suspend fun getSchedule(
        type: ScheduleType,
        name: String,
        weekOffset: Int
    ): Result<Schedule> =
        runCatching {
            scheduleService.getSchedule(
                type = type.pathName,
                name = name,
                weekOffset = weekOffset,
            )
        }
            .onSuccess { scheduleCacheWrapper.save(it) }
            .recoverCatching { scheduleCacheWrapper.restore(name, weekOffset).getOrThrow() }

    override fun setSelectedSchedule(selectedSchedule: SelectedSchedule) {
        this._selectedSchedule = selectedSchedule
        FirebaseCrashlytics.getInstance()
            .setCustomKey("schedule_name", selectedSchedule.name)
    }

    override fun getSelectedSchedule(): SelectedSchedule = this._selectedSchedule

    override fun getSelectedScheduleNameForAnalytics(): String? =
        runCatching { _selectedSchedule }.getOrNull()?.name

    override fun getSelectedScheduleImmediately(): SelectedSchedule =
        _selectedSchedule
}
