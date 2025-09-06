package kekmech.ru.feature_dashboard_impl.presentation.screen.main.elm

import kekmech.ru.ext_kotlin.fastLazy
import kekmech.ru.feature_dashboard_impl.presentation.screen.main.elm.DashboardEvent
import kekmech.ru.feature_schedule_api.PreheatSelectedScheduleProvider
import money.vivid.elmslie.core.store.ElmStore
import money.vivid.elmslie.core.store.Store
import kekmech.ru.feature_dashboard_impl.presentation.screen.main.elm.DashboardEffect as Effect
import kekmech.ru.feature_dashboard_impl.presentation.screen.main.elm.DashboardEvent as Event
import kekmech.ru.feature_dashboard_impl.presentation.screen.main.elm.DashboardState as State

internal typealias DashboardStore = Store<Event, Effect, State>

internal class DashboardStoreProvider(
    private val actor: DashboardActor,
    private val preheatSelectedScheduleProvider: PreheatSelectedScheduleProvider,
) {

    private val store by fastLazy {
        ElmStore(
            initialState = State(selectedSchedule = preheatSelectedScheduleProvider.getSelectedScheduleImmediately()),
            reducer = DashboardReducer(),
            actor = actor,
            startEvent = DashboardEvent.Ui.Init,
        )
    }

    fun get(): DashboardStore = store
}
