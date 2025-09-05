package kekmech.ru.feature_dashboard.screens.main.elm

import kekmech.ru.domain_schedule.PreheatSelectedScheduleProvider
import money.vivid.elmslie.core.store.ElmStore
import kekmech.ru.feature_dashboard.screens.main.elm.DashboardCommand as Command
import kekmech.ru.feature_dashboard.screens.main.elm.DashboardEffect as Effect
import kekmech.ru.feature_dashboard.screens.main.elm.DashboardEvent as Event
import kekmech.ru.feature_dashboard.screens.main.elm.DashboardState as State

internal class DashboardFeatureFactory(
    private val actor: DashboardActor,
    private val preheatSelectedScheduleProvider: PreheatSelectedScheduleProvider,
) {

    fun create(): ElmStore<Event, State, Effect, Command> {
        val preheatSelectedSchedule =
            preheatSelectedScheduleProvider.getSelectedScheduleImmediately()
        return ElmStore(
            initialState = State(selectedSchedule = preheatSelectedSchedule),
            reducer = DashboardReducer(),
            actor = actor,
        )
    }
}
