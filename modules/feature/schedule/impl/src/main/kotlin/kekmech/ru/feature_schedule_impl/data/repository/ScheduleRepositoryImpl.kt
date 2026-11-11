package kekmech.ru.feature_schedule_impl.data.repository

import com.google.firebase.crashlytics.FirebaseCrashlytics
import kekmech.ru.feature_schedule_api.PreheatSelectedScheduleProvider
import kekmech.ru.feature_schedule_api.data.repository.ScheduleRepository
import kekmech.ru.feature_schedule_api.domain.model.ScheduleKey
import kekmech.ru.feature_schedule_api.domain.model.ScheduleResource
import kekmech.ru.feature_schedule_api.domain.model.SelectedSchedule
import kekmech.ru.feature_schedule_impl.data.datasource.CachedScheduleSource
import kekmech.ru.feature_schedule_impl.data.datasource.SelectedScheduleSource
import kekmech.ru.lib_analytics_api.SelectedScheduleAnalyticsProvider
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.filterNotNull

internal class ScheduleRepositoryImpl(
    private val cachedScheduleSource: CachedScheduleSource,
    selectedScheduleSource: SelectedScheduleSource,
) :
    ScheduleRepository,
    SelectedScheduleAnalyticsProvider,
    PreheatSelectedScheduleProvider {

    private var _selectedSchedule: SelectedSchedule by selectedScheduleSource

    private val selectedScheduleFlow by lazy {
        MutableStateFlow(runCatching { _selectedSchedule }.getOrNull())
    }

    override fun observeSchedule(key: ScheduleKey): Flow<ScheduleResource> =
        cachedScheduleSource.observe(key)

    override suspend fun refreshSchedule(key: ScheduleKey): Result<Unit> =
        cachedScheduleSource.refresh(key)

    override fun observeSelectedSchedule(): Flow<SelectedSchedule> =
        selectedScheduleFlow.filterNotNull()

    override fun setSelectedSchedule(selectedSchedule: SelectedSchedule) {
        this._selectedSchedule = selectedSchedule
        selectedScheduleFlow.value = selectedSchedule
        FirebaseCrashlytics.getInstance()
            .setCustomKey("schedule_name", selectedSchedule.name)
    }

    override fun getSelectedSchedule(): SelectedSchedule = this._selectedSchedule

    override fun getSelectedScheduleNameForAnalytics(): String? =
        runCatching { _selectedSchedule }.getOrNull()?.name

    override fun getSelectedScheduleImmediately(): SelectedSchedule =
        _selectedSchedule
}
