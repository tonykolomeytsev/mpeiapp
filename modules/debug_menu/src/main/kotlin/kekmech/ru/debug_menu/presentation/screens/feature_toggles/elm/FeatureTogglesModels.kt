package kekmech.ru.debug_menu.presentation.screens.feature_toggles.elm

import kekmech.ru.common_elm.Resource
import vivid.money.elmslie.core.store.Store

internal typealias FeatureTogglesStore =
        Store<FeatureTogglesEvent, FeatureTogglesEffect, FeatureTogglesState>

internal data class FeatureTogglesState(
    val featureToggles: Resource<List<FeatureToggle>> = Resource.Loading,
)

internal data class FeatureToggle(
    val name: String,
    val value: Boolean,
    val overwritten: Boolean,
)

internal sealed interface FeatureTogglesEvent {

    sealed interface Ui : FeatureTogglesEvent {

        object Init : Ui

        sealed interface Click : Ui {

            data class Switch(val name: String, val value: Boolean) : Click
        }
    }

    sealed interface Internal : FeatureTogglesEvent {

        data class GetFeatureTogglesSuccess(
            val featureToggles: List<FeatureToggle>,
        ) : Internal

        data class RewriteFeatureToggleSuccess(
            val name: String,
            val value: Boolean,
            val overwritten: Boolean,
        ) : Internal
    }
}

internal sealed interface FeatureTogglesCommand {

    object GetFeatureToggles : FeatureTogglesCommand
    data class RewriteFeatureToggle(val name: String, val value: Boolean) : FeatureTogglesCommand
}

internal sealed interface FeatureTogglesEffect
