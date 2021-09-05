package kekmech.ru.feature_dashboard.elm

import vivid.money.elmslie.core.store.ElmStore

class DashboardFeatureFactory(
    private val actor: DashboardActor,
    private val reducer: DashboardReducer,
) {

    fun create() = ElmStore(
        initialState = DashboardState(),
        reducer = reducer,
        actor = actor
    )
}