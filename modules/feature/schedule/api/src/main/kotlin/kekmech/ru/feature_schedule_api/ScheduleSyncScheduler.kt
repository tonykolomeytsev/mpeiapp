package kekmech.ru.feature_schedule_api

/**
 * Downloads the selected and favorite schedules (this and the next two weeks) in the
 * background, so they're available offline. Besides [requestSync], it runs every night.
 */
public interface ScheduleSyncScheduler {

    /**
     * Download the selected and favorite schedules as soon as there is a connection, even if
     * the app is closed by then. Call it when the selected or favorite schedules change.
     */
    public fun requestSync()
}
