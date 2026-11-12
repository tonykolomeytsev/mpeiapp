package kekmech.ru.feature_schedule_impl.data.datasource

import kekmech.ru.feature_schedule_api.domain.model.Schedule
import kekmech.ru.feature_schedule_api.domain.model.ScheduleKey
import kekmech.ru.feature_schedule_api.domain.model.ScheduleRefreshState
import kekmech.ru.feature_schedule_api.domain.model.ScheduleResource
import kekmech.ru.feature_schedule_impl.data.database.ScheduleWeekDao
import kekmech.ru.feature_schedule_impl.data.database.ScheduleWeekEntity
import kekmech.ru.feature_schedule_impl.data.mapper.ScheduleMapper
import kekmech.ru.feature_schedule_impl.data.model.ScheduleDto
import kekmech.ru.feature_schedule_impl.data.network.ScheduleService
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import java.time.Clock
import java.time.DayOfWeek
import java.time.Instant
import java.time.LocalDate
import java.time.temporal.ChronoUnit
import java.util.Locale
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.atomic.AtomicBoolean
import kotlin.time.Duration
import kotlin.time.Duration.Companion.days
import kotlin.time.Duration.Companion.minutes

/**
 * Cache-first schedule source, the implementation of
 * [kekmech.ru.feature_schedule_api.data.repository.ScheduleRepository.observeSchedule].
 *
 * - Room is the single source of truth: subscribers only ever read the database, downloads
 * only ever write to it.
 * - Every emission decodes a new [Schedule], so subscribers may mutate it (notes are attached
 * in place) without affecting each other.
 * - Downloads run in [scope] (the application scope), so a slow download through a VPN isn't
 * cancelled when the user leaves the screen.
 *
 * @param clock Moscow clock, used to convert weeks to the backend's week offsets
 */
@Suppress("TooManyFunctions")
internal class CachedScheduleSource(
    private val scheduleService: ScheduleService,
    private val scheduleWeekDao: ScheduleWeekDao,
    private val json: Json,
    private val scope: CoroutineScope,
    private val dispatcher: CoroutineDispatcher,
    private val clock: Clock,
    private val staleAfter: Duration = DefaultStaleAfter,
) {

    /** Refresh state of each week, missing key means [ScheduleRefreshState.Idle] */
    private val refreshStates = MutableStateFlow<Map<ScheduleKey, ScheduleRefreshState>>(emptyMap())

    /** Running downloads, one per week */
    private val downloads = ConcurrentHashMap<ScheduleKey, Deferred<Result<Unit>>>()

    private val isPruned = AtomicBoolean(false)

    fun observe(key: ScheduleKey): Flow<ScheduleResource> {
        val id = key.storageId()
        val entities = scheduleWeekDao
            .observe(
                type = id.type.name,
                name = id.name,
                firstDayOfWeek = id.firstDayOfWeek.toEpochDay(),
            )
            .distinctUntilChanged()
        val refreshState = refreshStates
            .map { it[id] ?: ScheduleRefreshState.Idle }
            .distinctUntilChanged()
        return combine(entities, refreshState) { entity, refresh ->
            ScheduleResource(
                key = key,
                value = entity?.decodeOrNull(),
                updatedAt = entity?.updatedAt?.let(Instant::ofEpochMilli),
                refresh = refresh,
            )
        }
            .onStart { refreshInBackgroundIfStale(key) }
            .flowOn(dispatcher)
    }

    suspend fun refresh(key: ScheduleKey): Result<Unit> {
        val id = key.storageId()
        val download = downloads.computeIfAbsent(id) {
            scope.async(dispatcher, start = CoroutineStart.LAZY) {
                try {
                    download(key)
                } finally {
                    downloads.remove(id)
                }
            }
        }
        download.start()
        return download.await()
    }

    private fun refreshInBackgroundIfStale(key: ScheduleKey) {
        scope.launch(dispatcher) {
            val id = key.storageId()
            val updatedAt = scheduleWeekDao.getUpdatedAt(
                type = id.type.name,
                name = id.name,
                firstDayOfWeek = id.firstDayOfWeek.toEpochDay(),
            )
            val isStale = updatedAt == null ||
                clock.millis() - updatedAt >= staleAfter.inWholeMilliseconds
            if (isStale) refresh(key)
        }
    }

    @Suppress("TooGenericExceptionCaught")
    private suspend fun download(key: ScheduleKey): Result<Unit> {
        val id = key.storageId()
        setRefreshState(id, ScheduleRefreshState.InProgress)
        val result = try {
            val dto = scheduleService.getSchedule(
                type = key.type.pathName,
                name = key.name,
                weekOffset = ChronoUnit.WEEKS.between(currentMonday(), key.firstDayOfWeek).toInt(),
            )
            // Store the week under the Monday the backend actually returned: around midnight
            // on Monday the backend and the device may disagree about the current week
            val returnedMonday = dto.weeks.firstOrNull()?.firstDayOfWeek ?: key.firstDayOfWeek
            scheduleWeekDao.upsert(
                ScheduleWeekEntity(
                    type = id.type.name,
                    name = id.name,
                    firstDayOfWeek = returnedMonday.toEpochDay(),
                    payload = json.encodeToString(ScheduleDto.serializer(), dto),
                    updatedAt = clock.millis(),
                ),
            )
            check(returnedMonday == key.firstDayOfWeek) {
                "Requested the week of ${key.firstDayOfWeek}, received the week of $returnedMonday"
            }
            Result.success(Unit)
        } catch (e: CancellationException) {
            setRefreshState(id, ScheduleRefreshState.Idle)
            throw e
        } catch (e: Exception) {
            Result.failure(e)
        }
        setRefreshState(
            id = id,
            state = result.fold(
                onSuccess = { ScheduleRefreshState.Idle },
                onFailure = { ScheduleRefreshState.Failed(error = it, at = clock.instant()) },
            ),
        )
        if (result.isSuccess) pruneOnce()
        return result
    }

    private fun setRefreshState(id: ScheduleKey, state: ScheduleRefreshState) {
        refreshStates.update { states ->
            if (state is ScheduleRefreshState.Idle) states - id else states + (id to state)
        }
    }

    /** Clean the database up once per process, after the first successful download */
    private suspend fun pruneOnce() {
        if (!isPruned.compareAndSet(false, true)) return
        runCatching {
            scheduleWeekDao.deleteOutdated(
                firstDayOfWeekBefore = currentMonday().minusWeeks(KeepPastWeeks).toEpochDay(),
                updatedBefore = clock.millis() - KeepUnusedFor.inWholeMilliseconds,
            )
        }
    }

    private fun ScheduleWeekEntity.decodeOrNull(): Schedule? =
        runCatching {
            ScheduleMapper.dtoToDomain(json.decodeFromString(ScheduleDto.serializer(), payload))
        }.getOrNull()

    private fun currentMonday(): LocalDate = LocalDate.now(clock).with(DayOfWeek.MONDAY)

    /** The same week of the same schedule, whatever the case of the name */
    private fun ScheduleKey.storageId(): ScheduleKey = copy(name = name.trim().uppercase(Locale.ROOT))

    companion object {

        val DefaultStaleAfter = 5.minutes
        private const val KeepPastWeeks = 8L
        private val KeepUnusedFor = 60.days
    }
}
