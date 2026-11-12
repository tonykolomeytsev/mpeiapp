package kekmech.ru.feature_schedule_api.domain.model

import java.time.Instant

/**
 * Cached state of one week of a schedule, see
 * [kekmech.ru.feature_schedule_api.data.repository.ScheduleRepository.observeSchedule].
 *
 * [value] and [refresh] are independent: "saved schedule + the last refresh failed" is a normal
 * state, show the schedule in that case.
 */
public data class ScheduleResource(
    val key: ScheduleKey,
    /** Last successfully downloaded schedule. Null only if this week was never downloaded. */
    val value: Schedule?,
    /** When [value] was downloaded. */
    val updatedAt: Instant?,
    val refresh: ScheduleRefreshState,
) {

    /** Nothing to show and the last refresh failed. */
    val error: Throwable?
        get() = (refresh as? ScheduleRefreshState.Failed)?.error?.takeIf { value == null }

    /** There is a saved schedule to show, but it couldn't be updated. */
    val isSavedAfterFailedRefresh: Boolean
        get() = value != null && refresh is ScheduleRefreshState.Failed

    /** The week is either loaded or failed to load, nothing else will happen without a refresh. */
    val isSettled: Boolean
        get() = value != null || refresh is ScheduleRefreshState.Failed
}

public sealed interface ScheduleRefreshState {

    public data object Idle : ScheduleRefreshState

    public data object InProgress : ScheduleRefreshState

    /** Sticky until the next refresh of the same week starts. */
    public data class Failed(val error: Throwable, val at: Instant) : ScheduleRefreshState
}
