package kekmech.ru.debug_menu.presentation.screens.feature_toggles.elm

import vivid.money.elmslie.coroutines.ElmStoreCompat

internal class FeatureTogglesStoreFactory(
    private val actor: FeatureTogglesActor,
) {

    fun create(): FeatureTogglesStore =
        ElmStoreCompat(
            initialState = FeatureTogglesState(),
            reducer = FeatureTogglesReducer(),
            actor = actor,
            startEvent = FeatureTogglesEvent.Ui.Init,
        )
}
