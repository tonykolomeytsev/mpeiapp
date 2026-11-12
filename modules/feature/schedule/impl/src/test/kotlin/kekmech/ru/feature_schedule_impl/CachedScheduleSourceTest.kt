package kekmech.ru.feature_schedule_impl

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.booleans.shouldBeTrue
import io.kotest.matchers.collections.shouldBeEmpty
import io.kotest.matchers.collections.shouldContainExactly
import io.kotest.matchers.maps.shouldContainKey
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeTypeOf
import kekmech.ru.ext_json.LocalDateSerializer
import kekmech.ru.ext_json.LocalTimeSerializer
import kekmech.ru.feature_schedule_api.domain.model.ScheduleKey
import kekmech.ru.feature_schedule_api.domain.model.ScheduleRefreshState
import kekmech.ru.feature_schedule_api.domain.model.ScheduleResource
import kekmech.ru.feature_schedule_api.domain.model.ScheduleType
import kekmech.ru.feature_schedule_api.domain.model.ScheduleType.GROUP
import kekmech.ru.feature_schedule_impl.data.database.ScheduleWeekDao
import kekmech.ru.feature_schedule_impl.data.database.ScheduleWeekEntity
import kekmech.ru.feature_schedule_impl.data.datasource.CachedScheduleSource
import kekmech.ru.feature_schedule_impl.data.model.DayDto
import kekmech.ru.feature_schedule_impl.data.model.GetSearchResultsResponse
import kekmech.ru.feature_schedule_impl.data.model.ScheduleDto
import kekmech.ru.feature_schedule_impl.data.model.ScheduleTypeDto
import kekmech.ru.feature_schedule_impl.data.model.WeekDto
import kekmech.ru.feature_schedule_impl.data.network.ScheduleService
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.SerializersModule
import java.io.IOException
import java.time.Clock
import java.time.LocalDate
import java.time.LocalTime
import java.time.ZoneId
import java.time.ZonedDateTime
import kotlin.time.Duration.Companion.minutes

@OptIn(ExperimentalCoroutinesApi::class)
internal class CachedScheduleSourceTest : StringSpec({

    "Fresh saved week is emitted without going to the network" {
        runTest {
            val env = Env(this)
            env.dao.save(entity(CurrentMonday, id = "saved", updatedAt = Now.minusMinutes(1)))

            val emissions = env.collect(CurrentWeek)
            runCurrent()

            emissions.last().value?.id shouldBe "saved"
            emissions.last().refresh shouldBe ScheduleRefreshState.Idle
            env.service.requestedOffsets.shouldBeEmpty()
        }
    }

    "Saved week is found whatever the case of the name" {
        runTest {
            val env = Env(this)
            env.dao.save(entity(CurrentMonday, id = "saved", updatedAt = Now.minusMinutes(1)))

            val emissions = env.collect(CurrentWeek.copy(name = "с-12-16"))
            runCurrent()

            emissions.last().value?.id shouldBe "saved"
        }
    }

    "Missing week is downloaded and emitted" {
        runTest {
            val env = Env(this)
            env.service.respond = { dto(CurrentMonday, id = "downloaded") }

            val emissions = env.collect(CurrentWeek)
            runCurrent()

            emissions.first().value.shouldBeNull()
            emissions.last().value?.id shouldBe "downloaded"
            emissions.last().refresh shouldBe ScheduleRefreshState.Idle
            env.service.requestedOffsets.shouldContainExactly(0)
        }
    }

    "Stale saved week is emitted first, then replaced by the downloaded one" {
        runTest {
            val env = Env(this)
            env.dao.save(entity(CurrentMonday, id = "saved", updatedAt = Now.minusMinutes(10)))
            val response = CompletableDeferred<ScheduleDto>()
            env.service.respond = { response.await() }

            val emissions = env.collect(CurrentWeek)
            runCurrent()

            // the network is slow, the saved week is already shown
            emissions.last().value?.id shouldBe "saved"
            emissions.last().refresh shouldBe ScheduleRefreshState.InProgress

            response.complete(dto(CurrentMonday, id = "downloaded"))
            runCurrent()

            emissions.last().value?.id shouldBe "downloaded"
            emissions.last().refresh shouldBe ScheduleRefreshState.Idle
        }
    }

    "Failed refresh keeps the saved week" {
        runTest {
            val env = Env(this)
            env.dao.save(entity(CurrentMonday, id = "saved", updatedAt = Now.minusMinutes(10)))
            env.service.respond = { throw IOException("VPN is too slow") }

            val emissions = env.collect(CurrentWeek)
            runCurrent()

            val last = emissions.last()
            last.value?.id shouldBe "saved"
            last.refresh.shouldBeTypeOf<ScheduleRefreshState.Failed>()
            last.isSavedAfterFailedRefresh.shouldBeTrue()
            last.error.shouldBeNull()
        }
    }

    "Concurrent refreshes of the same week share one request" {
        runTest {
            val env = Env(this)
            val response = CompletableDeferred<ScheduleDto>()
            env.service.respond = { response.await() }

            env.collect(CurrentWeek) // background refresh, the week is missing
            val first = async { env.source.refresh(CurrentWeek) }
            val second = async { env.source.refresh(CurrentWeek.copy(name = "с-12-16")) }
            runCurrent()
            response.complete(dto(CurrentMonday, id = "downloaded"))
            runCurrent()

            env.service.requestedOffsets.shouldContainExactly(0)
            first.await().isSuccess.shouldBeTrue()
            second.await().isSuccess.shouldBeTrue()
        }
    }

    "Download isn't cancelled when the subscriber leaves" {
        runTest {
            val env = Env(this)
            val response = CompletableDeferred<ScheduleDto>()
            env.service.respond = { response.await() }

            val subscriber = backgroundScope.launch(env.dispatcher) { env.source.observe(CurrentWeek).toList() }
            runCurrent()
            subscriber.cancel()
            response.complete(dto(CurrentMonday, id = "downloaded"))
            runCurrent()

            env.dao.rows.value shouldContainKey Triple(GROUP.name, "С-12-16", CurrentMonday.toEpochDay())
        }
    }

    "Next week is requested with offset 1, a different returned week fails the request" {
        runTest {
            val env = Env(this)
            val nextMonday = CurrentMonday.plusWeeks(1)
            // the backend still thinks it's the current week
            env.service.respond = { dto(CurrentMonday, id = "current") }

            val emissions = env.collect(CurrentWeek.copy(firstDayOfWeek = nextMonday))
            runCurrent()

            env.service.requestedOffsets.shouldContainExactly(1)
            emissions.last().value.shouldBeNull()
            emissions.last().refresh.shouldBeTypeOf<ScheduleRefreshState.Failed>()
            // the returned week is still useful, it's saved under its own Monday
            env.dao.rows.value shouldContainKey Triple(GROUP.name, "С-12-16", CurrentMonday.toEpochDay())
        }
    }
}) {

    private class Env(testScope: TestScope) {
        val dispatcher = StandardTestDispatcher(testScope.testScheduler)
        val dao = FakeScheduleWeekDao()
        val service = FakeScheduleService()
        private val backgroundScope = testScope.backgroundScope
        val source = CachedScheduleSource(
            scheduleService = service,
            scheduleWeekDao = dao,
            json = TestJson,
            scope = backgroundScope,
            dispatcher = dispatcher,
            clock = Clock.fixed(Now.toInstant(), MoscowZone),
            staleAfter = 5.minutes,
        )

        fun collect(key: ScheduleKey): List<ScheduleResource> {
            val emissions = mutableListOf<ScheduleResource>()
            backgroundScope.launch(dispatcher) { source.observe(key).toList(emissions) }
            return emissions
        }
    }

    private class FakeScheduleService : ScheduleService {

        val requestedOffsets = mutableListOf<Int>()
        var respond: suspend (weekOffset: Int) -> ScheduleDto = { error("Unexpected request") }

        override suspend fun getSchedule(type: String, name: String, weekOffset: Int): ScheduleDto {
            requestedOffsets += weekOffset
            return respond(weekOffset)
        }

        override suspend fun getSearchResults(query: String, type: ScheduleType?): GetSearchResultsResponse =
            error("Unexpected request")
    }

    private class FakeScheduleWeekDao : ScheduleWeekDao {

        val rows = MutableStateFlow<Map<Triple<String, String, Long>, ScheduleWeekEntity>>(emptyMap())

        fun save(entity: ScheduleWeekEntity) =
            rows.update { it + (Triple(entity.type, entity.name, entity.firstDayOfWeek) to entity) }

        override fun observe(type: String, name: String, firstDayOfWeek: Long): Flow<ScheduleWeekEntity?> =
            rows.map { it[Triple(type, name, firstDayOfWeek)] }

        override suspend fun getUpdatedAt(type: String, name: String, firstDayOfWeek: Long): Long? =
            rows.value[Triple(type, name, firstDayOfWeek)]?.updatedAt

        override suspend fun upsert(entity: ScheduleWeekEntity) = save(entity)

        override suspend fun deleteOutdated(firstDayOfWeekBefore: Long, updatedBefore: Long) =
            rows.update { rows ->
                rows.filterValues { it.firstDayOfWeek >= firstDayOfWeekBefore && it.updatedAt >= updatedBefore }
            }
    }

    private companion object {

        val MoscowZone: ZoneId = ZoneId.of("Europe/Moscow")

        /** Wednesday */
        val Now = LocalDate.of(2026, 11, 11).atTime(12, 0).atZone(MoscowZone)
        val CurrentMonday: LocalDate = LocalDate.of(2026, 11, 9)
        val CurrentWeek = ScheduleKey(GROUP, "С-12-16", CurrentMonday)

        val TestJson = Json {
            encodeDefaults = true
            ignoreUnknownKeys = true
            coerceInputValues = true
            explicitNulls = false
            serializersModule = SerializersModule {
                contextual(LocalDate::class, LocalDateSerializer)
                contextual(LocalTime::class, LocalTimeSerializer)
            }
        }

        fun dto(monday: LocalDate, id: String) = ScheduleDto(
            name = "С-12-16",
            id = id,
            type = ScheduleTypeDto.GROUP,
            weeks = listOf(
                WeekDto(
                    weekOfYear = 46,
                    weekOfSemester = 11,
                    firstDayOfWeek = monday,
                    days = listOf(DayDto(dayOfWeek = 1, date = monday, classes = emptyList())),
                ),
            ),
        )

        fun entity(monday: LocalDate, id: String, updatedAt: ZonedDateTime) =
            ScheduleWeekEntity(
                type = GROUP.name,
                name = "С-12-16",
                firstDayOfWeek = monday.toEpochDay(),
                payload = TestJson.encodeToString(ScheduleDto.serializer(), dto(monday, id)),
                updatedAt = updatedAt.toInstant().toEpochMilli(),
            )
    }
}
