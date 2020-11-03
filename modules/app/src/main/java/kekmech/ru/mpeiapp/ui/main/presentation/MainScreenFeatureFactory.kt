package kekmech.ru.mpeiapp.ui.main.presentation

import kekmech.ru.common_mvi.BaseFeature

object MainScreenFeatureFactory {

    fun create(): MainScreenFeature = BaseFeature(
        initialState = MainScreenState(),
        reducer = MainScreenReducer(),
        actor = MainScreenActor()
    ).start()
}