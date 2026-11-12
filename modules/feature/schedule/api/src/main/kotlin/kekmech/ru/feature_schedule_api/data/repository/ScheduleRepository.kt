package kekmech.ru.feature_schedule_api.data.repository

import kekmech.ru.feature_schedule_api.domain.model.ScheduleKey
import kekmech.ru.feature_schedule_api.domain.model.ScheduleResource
import kekmech.ru.feature_schedule_api.domain.model.SelectedSchedule
import kotlinx.coroutines.flow.Flow

public interface ScheduleRepository {

    /**
     * Cache-first subscription to one week of a schedule.
     *
     * - The first emission is the saved state, read from disk without waiting for the network.
     * - On every subscription the week is refreshed in the background if it was never
     * downloaded or the saved copy is older than a few minutes.
     * - A failed refresh never removes [ScheduleResource.value].
     * - Downloads run in the application scope: unsubscribing doesn't cancel a download in
     * progress, it still lands in the cache.
     */
    public fun observeSchedule(key: ScheduleKey): Flow<ScheduleResource>

    /**
     * Download the week now, however fresh the saved copy is. Concurrent calls (and the
     * background refreshes of [observeSchedule]) for the same week share one request.
     * Subscribers of [observeSchedule] receive the result.
     */
    public suspend fun refreshSchedule(key: ScheduleKey): Result<Unit>

    /** Emits the selected schedule and every change of it. Doesn't emit before onboarding. */
    public fun observeSelectedSchedule(): Flow<SelectedSchedule>

    public fun setSelectedSchedule(selectedSchedule: SelectedSchedule)

    /**
     * @throws IllegalStateException If schedule isn't selected yet (before onboarding)
     */
    public fun getSelectedSchedule(): SelectedSchedule
}
