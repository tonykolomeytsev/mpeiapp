package kekmech.ru.feature_dashboard.presentation

import kekmech.ru.common_mvi.BaseFeature

class DashboardFeatureFactory(
    private val actor: DashboardActor,
    private val reducer: DashboardReducer
) {

    fun create() = BaseFeature(
        initialState = DashboardState(),
        reducer = reducer,
        actor = actor
    ).start()
}