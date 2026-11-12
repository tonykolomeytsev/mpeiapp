package kekmech.ru.feature_schedule_impl.sync

import kekmech.ru.feature_favorite_schedule_api.data.repository.FavoriteScheduleRepository
import kekmech.ru.feature_schedule_api.data.repository.ScheduleRepository
import kekmech.ru.feature_schedule_api.domain.model.ScheduleKey
import kekmech.ru.feature_schedule_api.domain.model.ScheduleType
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.sync.Semaphore
import kotlinx.coroutines.sync.withPermit
import java.time.LocalDate
import java.time.ZoneId
import java.util.Locale

/**
 * Downloads this and the next two weeks of the selected and favorite schedules into the cache.
 * Weeks are downloaded even if the saved copy is fresh: the sync runs rarely and the next
 * time the user opens the app there may be no connection at all.
 *
 * @param today today in Moscow, the weeks are counted from it
 */
internal class SyncSchedulesUseCase(
    private val scheduleRepository: ScheduleRepository,
    private val favoriteScheduleRepository: FavoriteScheduleRepository,
    private val today: () -> LocalDate = { LocalDate.now(ZoneId.of("Europe/Moscow")) },
) {

    /**
     * @return true if every week was downloaded (or there was nothing to download)
     */
    suspend operator fun invoke(): Boolean {
        val today = today()
        val keys = getSchedulesToSync().flatMap { (type, name) ->
            SyncedWeekOffsets.map { weekOffset ->
                ScheduleKey.of(type = type, name = name, weekOffset = weekOffset, today = today)
            }
        }
        // Don't flood a slow connection (and the backend at night) with parallel requests
        val semaphore = Semaphore(MaxParallelDownloads)
        val results = coroutineScope {
            keys
                .map { key -> async { semaphore.withPermit { scheduleRepository.refreshSchedule(key) } } }
                .awaitAll()
        }
        return results.all { it.isSuccess }
    }

    private suspend fun getSchedulesToSync(): List<Pair<ScheduleType, String>> {
        val selected = getOrNull { scheduleRepository.getSelectedSchedule() } // null before onboarding
            ?.let { listOf(it.type to it.name) }
            .orEmpty()
        val favorites = getOrNull { favoriteScheduleRepository.getAllFavorites() }
            .orEmpty()
            .map { it.type to it.name }
        return (selected + favorites).distinctBy { (type, name) -> type to name.uppercase(Locale.ROOT) }
    }

    @Suppress("TooGenericExceptionCaught")
    private inline fun <T> getOrNull(block: () -> T): T? =
        try {
            block()
        } catch (e: CancellationException) {
            throw e
        } catch (_: Exception) {
            null
        }

    private companion object {

        val SyncedWeekOffsets = 0..2
        const val MaxParallelDownloads = 2
    }
}
