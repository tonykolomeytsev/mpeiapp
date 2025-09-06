package kekmech.ru.feature_dashboard_impl.presentation.screen.main.elm

import kekmech.ru.feature_dashboard_impl.presentation.screen.main.elm.DashboardEvent
import kekmech.ru.feature_schedule_api.PreheatSelectedScheduleProvider
import money.vivid.elmslie.core.store.ElmStore
import money.vivid.elmslie.core.store.Store
import kekmech.ru.feature_dashboard_impl.presentation.screen.main.elm.DashboardEffect as Effect
import kekmech.ru.feature_dashboard_impl.presentation.screen.main.elm.DashboardEvent as Event
import kekmech.ru.feature_dashboard_impl.presentation.screen.main.elm.DashboardState as State

internal typealias DashboardStore = Store<Event, Effect, State>

internal class DashboardStoreFactory(
    private val actor: DashboardActor,
    private val preheatSelectedScheduleProvider: PreheatSelectedScheduleProvider,
) {

    fun create(): DashboardStore = ElmStore(
        initialState = State(selectedSchedule = preheatSelectedScheduleProvider.getSelectedScheduleImmediately()),
        reducer = DashboardReducer(),
        actor = actor,
        startEvent = DashboardEvent.Ui.Init,
    )
}
