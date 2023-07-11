package kekmech.ru.feature_schedule_impl.presentation.screen.main.elm

import kekmech.ru.feature_notes_api.domain.service.AttachNotesToScheduleService
import kekmech.ru.feature_schedule_api.domain.usecase.GetCurrentScheduleUseCase
import kekmech.ru.lib_elm.actorFlow
import kotlinx.coroutines.flow.Flow
import vivid.money.elmslie.coroutines.Actor
import kekmech.ru.feature_schedule_impl.presentation.screen.main.elm.ScheduleCommand as Command
import kekmech.ru.feature_schedule_impl.presentation.screen.main.elm.ScheduleEvent as Event

internal class ScheduleActor(
    private val getCurrentScheduleUseCase: GetCurrentScheduleUseCase,
    private val attachNotesToScheduleService: AttachNotesToScheduleService,
) : Actor<Command, Event> {

    override fun execute(command: Command): Flow<Event> =
        when (command) {
            is Command.LoadSchedule -> actorFlow {
                val currentSchedule =
                    getCurrentScheduleUseCase.getSchedule(weekOffset = command.weekOffset)
                attachNotesToScheduleService.attach(currentSchedule)
            }.mapEvents(
                eventMapper = { schedule ->
                    Event.Internal.LoadScheduleSuccess(
                        weekOffset = command.weekOffset,
                        schedule = schedule,
                    )
                },
                errorMapper = Event.Internal::LoadScheduleFailure,
            )
        }
}
