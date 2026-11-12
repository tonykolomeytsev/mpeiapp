package kekmech.ru.feature_schedule_impl

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.booleans.shouldBeFalse
import io.kotest.matchers.booleans.shouldBeTrue
import io.kotest.matchers.collections.shouldBeEmpty
import io.kotest.matchers.collections.shouldContainExactlyInAnyOrder
import io.kotest.matchers.ints.shouldBeExactly
import kekmech.ru.feature_favorite_schedule_api.data.repository.FavoriteScheduleRepository
import kekmech.ru.feature_favorite_schedule_api.domain.model.FavoriteSchedule
import kekmech.ru.feature_schedule_api.data.repository.ScheduleRepository
import kekmech.ru.feature_schedule_api.domain.model.ScheduleKey
import kekmech.ru.feature_schedule_api.domain.model.ScheduleResource
import kekmech.ru.feature_schedule_api.domain.model.ScheduleType
import kekmech.ru.feature_schedule_api.domain.model.ScheduleType.GROUP
import kekmech.ru.feature_schedule_api.domain.model.ScheduleType.PERSON
import kekmech.ru.feature_schedule_api.domain.model.SelectedSchedule
import kekmech.ru.feature_schedule_impl.sync.SyncSchedulesUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.test.runTest
import java.io.IOException
import java.time.LocalDate
import java.util.Collections

internal class SyncSchedulesUseCaseTest : StringSpec({

    "Selected and favorite schedules are synced for this and the next two weeks" {
        runTest {
            val scheduleRepository = FakeScheduleRepository(selected = SelectedSchedule("С-12-16", GROUP))
            val favorites = FakeFavoriteScheduleRepository(
                listOf(
                    favorite("Иванов Иван Иванович", PERSON),
                    favorite("с-12-16", GROUP), // the selected one, different case
                ),
            )

            val isSynced = SyncSchedulesUseCase(scheduleRepository, favorites) { Today }()

            isSynced.shouldBeTrue()
            scheduleRepository.refreshed.shouldContainExactlyInAnyOrder(
                ScheduleKey(GROUP, "С-12-16", Monday),
                ScheduleKey(GROUP, "С-12-16", Monday.plusWeeks(1)),
                ScheduleKey(GROUP, "С-12-16", Monday.plusWeeks(2)),
                ScheduleKey(PERSON, "Иванов Иван Иванович", Monday),
                ScheduleKey(PERSON, "Иванов Иван Иванович", Monday.plusWeeks(1)),
                ScheduleKey(PERSON, "Иванов Иван Иванович", Monday.plusWeeks(2)),
            )
        }
    }

    "Any failed week fails the sync, the other weeks are still downloaded" {
        runTest {
            val failing = ScheduleKey(GROUP, "С-12-16", Monday.plusWeeks(1))
            val scheduleRepository = FakeScheduleRepository(
                selected = SelectedSchedule("С-12-16", GROUP),
                failing = setOf(failing),
            )

            val isSynced = SyncSchedulesUseCase(scheduleRepository, FakeFavoriteScheduleRepository()) { Today }()

            isSynced.shouldBeFalse()
            scheduleRepository.refreshed.size shouldBeExactly 3
        }
    }

    "Before onboarding there is nothing to sync" {
        runTest {
            val scheduleRepository = FakeScheduleRepository(selected = null)

            val isSynced = SyncSchedulesUseCase(scheduleRepository, FakeFavoriteScheduleRepository()) { Today }()

            isSynced.shouldBeTrue()
            scheduleRepository.refreshed.shouldBeEmpty()
        }
    }

    "Broken favorites don't stop the selected schedule from syncing" {
        runTest {
            val scheduleRepository = FakeScheduleRepository(selected = SelectedSchedule("С-12-16", GROUP))
            val favorites = FakeFavoriteScheduleRepository(error = IllegalStateException("database is locked"))

            val isSynced = SyncSchedulesUseCase(scheduleRepository, favorites) { Today }()

            isSynced.shouldBeTrue()
            scheduleRepository.refreshed.size shouldBeExactly 3
        }
    }
}) {

    private class FakeScheduleRepository(
        private val selected: SelectedSchedule?,
        private val failing: Set<ScheduleKey> = emptySet(),
    ) : ScheduleRepository {

        val refreshed: MutableList<ScheduleKey> = Collections.synchronizedList(mutableListOf())

        override suspend fun refreshSchedule(key: ScheduleKey): Result<Unit> {
            refreshed += key
            return if (key in failing) Result.failure(IOException("timeout")) else Result.success(Unit)
        }

        override fun getSelectedSchedule(): SelectedSchedule =
            selected ?: throw IllegalStateException("SelectedSchedule is empty")

        override fun observeSchedule(key: ScheduleKey): Flow<ScheduleResource> = error("Unused")
        override fun observeSelectedSchedule(): Flow<SelectedSchedule> = error("Unused")
        override fun setSelectedSchedule(selectedSchedule: SelectedSchedule) = error("Unused")
    }

    private class FakeFavoriteScheduleRepository(
        private val favorites: List<FavoriteSchedule> = emptyList(),
        private val error: Exception? = null,
    ) : FavoriteScheduleRepository {

        override suspend fun getAllFavorites(): List<FavoriteSchedule> = error?.let { throw it } ?: favorites
        override suspend fun updateOrInsertFavorite(favoriteSchedule: FavoriteSchedule) = error("Unused")
        override suspend fun deleteFavorite(favoriteSchedule: FavoriteSchedule) = error("Unused")
    }

    private companion object {

        /** Wednesday */
        val Today: LocalDate = LocalDate.of(2026, 11, 11)
        val Monday: LocalDate = LocalDate.of(2026, 11, 9)

        fun favorite(name: String, type: ScheduleType) =
            FavoriteSchedule(name = name, type = type, description = "", order = 0)
    }
}
