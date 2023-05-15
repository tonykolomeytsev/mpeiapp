package kekmech.ru.feature_schedule.screens.main.elm

import io.reactivex.rxjava3.core.Observable
import kekmech.ru.domain_notes.services.AttachNotesToScheduleService
import kekmech.ru.domain_schedule.use_cases.GetCurrentScheduleUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.rx3.rxSingle
import vivid.money.elmslie.core.store.Actor
import kekmech.ru.feature_schedule.screens.main.elm.ScheduleCommand as Command
import kekmech.ru.feature_schedule.screens.main.elm.ScheduleEvent as Event

internal class ScheduleActor(
    private val getCurrentScheduleUseCase: GetCurrentScheduleUseCase,
    private val attachNotesToScheduleService: AttachNotesToScheduleService,
) : Actor<Command, Event> {

    override fun execute(command: Command): Observable<Event> =
        when (command) {
            is Command.LoadSchedule -> getCurrentScheduleUseCase
                .getSchedule(weekOffset = command.weekOffset)
                .flatMap {
                    rxSingle(Dispatchers.Unconfined) {
                        attachNotesToScheduleService.attach(it)
                    }
                }
                .mapEvents(
                    successEventMapper = { schedule ->
                        Event.Internal.LoadScheduleSuccess(
                            weekOffset = command.weekOffset,
                            schedule = schedule,
                        )
                    },
                    failureEventMapper = Event.Internal::LoadScheduleFailure,
                )
        }
}
