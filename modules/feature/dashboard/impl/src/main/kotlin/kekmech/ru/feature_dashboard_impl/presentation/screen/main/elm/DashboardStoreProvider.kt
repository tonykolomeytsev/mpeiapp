package kekmech.ru.feature_dashboard_impl.presentation.screen.main.elm

import kekmech.ru.domain_schedule.PreheatSelectedScheduleProvider
import kekmech.ru.ext_kotlin.fastLazy
import kekmech.ru.feature_dashboard_impl.presentation.screen.main.elm.DashboardEvent
import vivid.money.elmslie.core.store.Store
import vivid.money.elmslie.coroutines.ElmStoreCompat
import kekmech.ru.feature_dashboard_impl.presentation.screen.main.elm.DashboardEffect as Effect
import kekmech.ru.feature_dashboard_impl.presentation.screen.main.elm.DashboardEvent as Event
import kekmech.ru.feature_dashboard_impl.presentation.screen.main.elm.DashboardState as State

internal typealias DashboardStore = Store<Event, Effect, State>

internal class DashboardStoreProvider(
    private val actor: DashboardActor,
    private val preheatSelectedScheduleProvider: PreheatSelectedScheduleProvider,
) {

    private val store by fastLazy {
        ElmStoreCompat(
            initialState = State(selectedSchedule = preheatSelectedScheduleProvider.getSelectedScheduleImmediately()),
            reducer = DashboardReducer(),
            actor = actor,
            startEvent = DashboardEvent.Ui.Init,
        )
    }

    fun get(): DashboardStore = store
}
