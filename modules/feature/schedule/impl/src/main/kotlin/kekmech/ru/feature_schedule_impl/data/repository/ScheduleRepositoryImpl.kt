package kekmech.ru.feature_schedule_impl.data.repository

import com.google.firebase.crashlytics.FirebaseCrashlytics
import kekmech.ru.feature_schedule_api.PreheatSelectedScheduleProvider
import kekmech.ru.feature_schedule_api.data.repository.ScheduleRepository
import kekmech.ru.feature_schedule_api.domain.model.Schedule
import kekmech.ru.feature_schedule_api.domain.model.ScheduleType
import kekmech.ru.feature_schedule_api.domain.model.SelectedSchedule
import kekmech.ru.feature_schedule_impl.data.datasource.ScheduleCacheWrapper
import kekmech.ru.feature_schedule_impl.data.datasource.SelectedScheduleSource
import kekmech.ru.feature_schedule_impl.data.network.ScheduleService
import kekmech.ru.library_analytics_api.SelectedScheduleAnalyticsProvider

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
        weekOffset: Int,
    ): Result<Schedule> =
        runCatching {
            scheduleService.getSchedule(
                type = type.pathName,
                name = name,
                weekOffset = weekOffset,
            )
        }
            .onSuccess { scheduleCacheWrapper.save(it) }
            .recoverCatching { sourceThrowable ->
                scheduleCacheWrapper.restore(name, weekOffset)
                    .getOrNull()
                    ?: throw sourceThrowable
            }

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
