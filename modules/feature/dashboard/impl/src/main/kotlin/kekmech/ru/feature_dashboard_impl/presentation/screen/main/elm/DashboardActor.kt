package kekmech.ru.feature_dashboard_impl.presentation.screen.main.elm

import com.google.firebase.crashlytics.FirebaseCrashlytics
import kekmech.ru.feature_dashboard_impl.domain.interactor.GetUpcomingEventsInteractor
import kekmech.ru.feature_dashboard_impl.domain.model.UpcomingEventsPrediction
import kekmech.ru.feature_dashboard_impl.presentation.screen.main.elm.DashboardEvent.Internal
import kekmech.ru.feature_favorite_schedule_api.data.repository.FavoriteScheduleRepository
import kekmech.ru.feature_notes_api.interactors.GetActualNotesInteractor
import kekmech.ru.feature_schedule_api.data.repository.ScheduleRepository
import kekmech.ru.feature_schedule_api.domain.model.ScheduleKey
import kekmech.ru.feature_schedule_api.domain.model.ScheduleResource
import kekmech.ru.feature_schedule_api.domain.model.WeekOfSemester
import kekmech.ru.lib_elm.Resource
import kekmech.ru.lib_elm.actorFlow
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import money.vivid.elmslie.core.store.Actor
import kekmech.ru.feature_dashboard_impl.presentation.screen.main.elm.DashboardCommand as Command
import kekmech.ru.feature_dashboard_impl.presentation.screen.main.elm.DashboardEvent as Event

private const val ObserveScheduleKey = "ObserveSchedule"
private val ObservedWeekOffsets = listOf(0, 1)

internal class DashboardActor(
    private val scheduleRepository: ScheduleRepository,
    private val favoriteScheduleRepository: FavoriteScheduleRepository,
    private val getUpcomingEventsInteractor: GetUpcomingEventsInteractor,
    private val getActualNotesInteractor: GetActualNotesInteractor,
) : Actor<Command, Event>() {

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun execute(command: Command): Flow<Event> =
        when (command) {
            is Command.ObserveSelectedSchedule -> scheduleRepository
                .observeSelectedSchedule()
                .mapEvents(Internal::GetSelectedScheduleSuccess)

            is Command.ObserveSchedule -> scheduleRepository
                .observeSelectedSchedule()
                .flatMapLatest { selectedSchedule ->
                    // both weeks switch to the new schedule together
                    combine(
                        ObservedWeekOffsets.map { weekOffset ->
                            scheduleRepository.observeSchedule(ScheduleKey.of(selectedSchedule, weekOffset))
                        },
                    ) { it.toList() }
                }
                .map { weeks ->
                    Internal.ScheduleUpdated(
                        weekOfSemester = weeks.first().toWeekOfSemester(),
                        upcomingEvents = weeks.toUpcomingEvents(),
                    )
                }
                .mapEvents(
                    eventMapper = { it },
                    errorMapper = { error ->
                        FirebaseCrashlytics.getInstance().recordException(error)
                        Internal.ScheduleUpdated(
                            weekOfSemester = Resource.Error(error),
                            upcomingEvents = Resource.Error(error),
                        )
                    },
                )
                .switch(ObserveScheduleKey)

            is Command.RefreshSchedule -> flow {
                val selectedSchedule = scheduleRepository.getSelectedSchedule()
                val results = coroutineScope {
                    ObservedWeekOffsets
                        .map { weekOffset ->
                            async {
                                scheduleRepository.refreshSchedule(
                                    ScheduleKey.of(selectedSchedule, weekOffset),
                                )
                            }
                        }
                        .awaitAll()
                }
                emit(Internal.RefreshScheduleFinished(results.firstNotNullOfOrNull { it.exceptionOrNull() }))
            }.mapEvents(
                eventMapper = { it },
                errorMapper = { Internal.RefreshScheduleFinished(it) },
            )

            is Command.GetActualNotes -> actorFlow {
                getActualNotesInteractor.invoke()
            }.mapEvents(
                eventMapper = Internal::GetActualNotesSuccess,
                errorMapper = Internal::GetActualNotesFailure,
            )

            is Command.GetFavoriteSchedules -> actorFlow {
                favoriteScheduleRepository.getAllFavorites()
            }.mapEvents(
                eventMapper = Internal::GetFavoriteSchedulesSuccess,
                errorMapper = Internal::GetFavoriteSchedulesFailure,
            )

            is Command.SelectSchedule -> actorFlow {
                scheduleRepository.setSelectedSchedule(command.selectedSchedule)
            }.mapEvents({ Internal.SelectScheduleSuccess })
        }

    private fun ScheduleResource.toWeekOfSemester(): Resource<WeekOfSemester> {
        val schedule = value ?: return error?.let { Resource.Error(it) } ?: Resource.Loading
        return schedule.weeks.firstOrNull()
            ?.let { Resource.Data(it.weekOfSemester) }
            ?: Resource.Error(NoSuchElementException("The schedule has no weeks"))
    }

    /** Wait for both weeks, then predict from whatever was loaded */
    private suspend fun List<ScheduleResource>.toUpcomingEvents(): Resource<UpcomingEventsPrediction> {
        if (!all { it.isSettled }) return Resource.Loading
        val schedules = mapNotNull { it.value }
        if (schedules.isEmpty()) return Resource.Error(firstNotNullOf { it.error })
        return Resource.Data(getUpcomingEventsInteractor(schedules))
    }
}
