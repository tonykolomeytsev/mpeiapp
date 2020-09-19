package kekmech.ru.feature_dashboard.presentation

import kekmech.ru.common_mvi.BaseFeature

class DashboardFeatureFactory(
    private val actor: DashboardActor
) {

    fun create() = BaseFeature<DashboardState, DashboardEvent, DashboardEffect, DashboardAction>(
        initialState = DashboardState(),
        reducer = DashboardReducer(),
        actor = actor
    ).start()
}