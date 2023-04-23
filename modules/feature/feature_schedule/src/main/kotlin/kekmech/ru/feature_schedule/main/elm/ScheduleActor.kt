package kekmech.ru.feature_schedule.main.elm

import io.reactivex.rxjava3.core.Observable
import kekmech.ru.domain_notes.NotesScheduleTransformer
import kekmech.ru.domain_schedule.ScheduleRepository
import vivid.money.elmslie.core.store.Actor
import kekmech.ru.feature_schedule.main.elm.ScheduleCommand as Command
import kekmech.ru.feature_schedule.main.elm.ScheduleEvent as Event

internal class ScheduleActor(
    private val scheduleRepository: ScheduleRepository,
    private val notesScheduleTransformer: NotesScheduleTransformer,
) : Actor<Command, Event> {

    override fun execute(command: Command): Observable<Event> =
        when (command) {
            is Command.LoadSchedule -> scheduleRepository
                .loadSchedule(weekOffset = command.weekOffset)
                .flatMap(notesScheduleTransformer::transform)
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
