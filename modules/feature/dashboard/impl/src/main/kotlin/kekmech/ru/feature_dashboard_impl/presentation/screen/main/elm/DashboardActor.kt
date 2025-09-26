package kekmech.ru.feature_dashboard_impl.presentation.screen.main.elm

import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.google.firebase.crashlytics.recordException
import kekmech.ru.ext_okhttp.NoInternetConnectionException
import kekmech.ru.feature_dashboard_impl.domain.interactor.GetUpcomingEventsInteractor
import kekmech.ru.feature_dashboard_impl.presentation.screen.main.elm.DashboardEvent.Internal
import kekmech.ru.feature_favorite_schedule_api.data.repository.FavoriteScheduleRepository
import kekmech.ru.feature_notes_api.interactors.GetActualNotesInteractor
import kekmech.ru.feature_schedule_api.data.repository.ScheduleRepository
import kekmech.ru.feature_schedule_api.domain.usecase.GetCurrentScheduleUseCase
import kekmech.ru.lib_elm.actorFlow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import money.vivid.elmslie.core.store.Actor
import retrofit2.HttpException
import java.net.SocketException
import java.net.SocketTimeoutException
import javax.net.ssl.SSLHandshakeException
import kekmech.ru.feature_dashboard_impl.presentation.screen.main.elm.DashboardCommand as Command
import kekmech.ru.feature_dashboard_impl.presentation.screen.main.elm.DashboardEvent as Event

internal class DashboardActor(
    private val scheduleRepository: ScheduleRepository,
    private val favoriteScheduleRepository: FavoriteScheduleRepository,
    private val getUpcomingEventsInteractor: GetUpcomingEventsInteractor,
    private val getActualNotesInteractor: GetActualNotesInteractor,
    private val getCurrentScheduleUseCase: GetCurrentScheduleUseCase,
) : Actor<Command, Event>() {

    override fun execute(command: Command): Flow<Event> =
        when (command) {
            is Command.GetSelectedSchedule -> actorFlow {
                scheduleRepository.getSelectedSchedule()
            }.mapEvents(Internal::GetSelectedScheduleSuccess)

            is Command.GetWeekOfSemester -> actorFlow {
                getCurrentScheduleUseCase
                    .getSchedule(weekOffset = 0)
                    .weeks
                    .first()
                    .weekOfSemester
            }.mapEvents(
                eventMapper = Internal::GetWeekOfSemesterSuccess,
                errorMapper = Internal::GetWeekOfSemesterFailure,
            )

            is Command.GetUpcomingEvents -> actorFlow {
                getUpcomingEventsInteractor.invoke()
            }.catch { e ->
                when (e) {
                    is SocketException,
                    is SocketTimeoutException,
                    is HttpException,
                    is NoInternetConnectionException -> Unit
                    // unexpected error types
                    else -> FirebaseCrashlytics.getInstance().recordException(e)
                }
                throw e // always rethrow to process in ELM reducer
            }.mapEvents(
                eventMapper = Internal::GetUpcomingEventsSuccess,
                errorMapper = Internal::GetUpcomingEventsFailure,
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
}
