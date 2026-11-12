package kekmech.ru.feature_schedule_impl.presentation.screen.main.elm

import com.google.firebase.crashlytics.FirebaseCrashlytics
import kekmech.ru.feature_notes_api.domain.service.AttachNotesToScheduleService
import kekmech.ru.feature_schedule_api.data.repository.ScheduleRepository
import kekmech.ru.feature_schedule_api.domain.model.Schedule
import kekmech.ru.feature_schedule_api.domain.usecase.ObserveCurrentScheduleUseCase
import kekmech.ru.lib_elm.actorFlow
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import money.vivid.elmslie.core.store.Actor
import kekmech.ru.feature_schedule_impl.presentation.screen.main.elm.ScheduleCommand as Command
import kekmech.ru.feature_schedule_impl.presentation.screen.main.elm.ScheduleEvent as Event

internal class ScheduleActor(
    private val observeCurrentScheduleUseCase: ObserveCurrentScheduleUseCase,
    private val scheduleRepository: ScheduleRepository,
    private val attachNotesToScheduleService: AttachNotesToScheduleService,
) : Actor<Command, Event>() {

    override fun execute(command: Command): Flow<Event> =
        when (command) {
            is Command.ObserveSchedule -> observeCurrentScheduleUseCase(command.weekOffset)
                .map { resource ->
                    // every emission is a new Schedule instance, safe to attach notes in place
                    resource.value?.let { attachNotesSafely(it) }
                    resource
                }
                .mapEvents(
                    eventMapper = { Event.Internal.ScheduleUpdated(command.weekOffset, it) },
                    errorMapper = { Event.Internal.ObserveScheduleFailure(command.weekOffset, it) },
                )
                .switch(key = command.weekOffset)

            is Command.RefreshSchedule -> actorFlow {
                // failures are delivered to the subscribers as ScheduleRefreshState.Failed
                scheduleRepository.refreshSchedule(command.key)
            }.mapEvents()
        }

    /** Notes are secondary: show the schedule without them rather than not show it at all */
    @Suppress("TooGenericExceptionCaught")
    private suspend fun attachNotesSafely(schedule: Schedule) {
        try {
            attachNotesToScheduleService.attach(schedule)
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            FirebaseCrashlytics.getInstance().recordException(e)
        }
    }
}
