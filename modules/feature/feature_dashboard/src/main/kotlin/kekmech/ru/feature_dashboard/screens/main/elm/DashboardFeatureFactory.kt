package kekmech.ru.feature_dashboard.screens.main.elm

import kekmech.ru.domain_schedule.PreheatSelectedScheduleProvider
import kekmech.ru.feature_dashboard.screens.main.elm.DashboardEvent
import vivid.money.elmslie.core.store.ElmStore
import vivid.money.elmslie.core.store.Store
import vivid.money.elmslie.coroutines.ElmStoreCompat
import kekmech.ru.feature_dashboard.screens.main.elm.DashboardCommand as Command
import kekmech.ru.feature_dashboard.screens.main.elm.DashboardEffect as Effect
import kekmech.ru.feature_dashboard.screens.main.elm.DashboardEvent as Event
import kekmech.ru.feature_dashboard.screens.main.elm.DashboardState as State

internal class DashboardFeatureFactory(
    private val actor: DashboardActor,
    private val preheatSelectedScheduleProvider: PreheatSelectedScheduleProvider,
) {

    fun create(): Store<Event, Effect, State> {
        val preheatSelectedSchedule =
            preheatSelectedScheduleProvider.getSelectedScheduleImmediately()
        return ElmStoreCompat(
            initialState = State(selectedSchedule = preheatSelectedSchedule),
            reducer = DashboardReducer(),
            actor = actor,
            startEvent = DashboardEvent.Ui.Init,
        )
    }
}
