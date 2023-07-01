package kekmech.ru.debug_menu.presentation.screens.feature_toggles.elm

import kekmech.ru.common_elm.map
import kekmech.ru.common_elm.toResource
import kekmech.ru.debug_menu.presentation.screens.feature_toggles.elm.FeatureTogglesEvent.Internal
import kekmech.ru.debug_menu.presentation.screens.feature_toggles.elm.FeatureTogglesEvent.Ui
import vivid.money.elmslie.core.store.dsl_reducer.ScreenDslReducer
import kekmech.ru.debug_menu.presentation.screens.feature_toggles.elm.FeatureTogglesCommand as Command
import kekmech.ru.debug_menu.presentation.screens.feature_toggles.elm.FeatureTogglesEffect as Effect
import kekmech.ru.debug_menu.presentation.screens.feature_toggles.elm.FeatureTogglesEvent as Event
import kekmech.ru.debug_menu.presentation.screens.feature_toggles.elm.FeatureTogglesState as State

internal class FeatureTogglesReducer :
    ScreenDslReducer<Event, Ui, Internal, State, Effect, Command>(
        uiEventClass = Ui::class,
        internalEventClass = Internal::class,
    ) {

    override fun Result.internal(event: Internal) =
        when (event) {
            is Internal.GetFeatureTogglesSuccess -> {
                state { copy(featureToggles = event.featureToggles.toResource()) }
            }
            is Internal.RewriteFeatureToggleSuccess -> {
                val updatedFeatureToggles = state.featureToggles.map { list ->
                    list.map {
                        if (it.name == event.name) {
                            it.copy(
                                value = event.value,
                                overwritten = event.overwritten,
                            )
                        } else {
                            it
                        }
                    }
                }
                state { copy(featureToggles = updatedFeatureToggles) }
            }
        }

    override fun Result.ui(event: Ui) =
        when (event) {
            is Ui.Init -> {
                commands { +Command.GetFeatureToggles }
            }
            is Ui.Click.Switch -> {
                commands {
                    +Command.RewriteFeatureToggle(
                        name = event.name,
                        value = event.value,
                    )
                }
            }
        }
}
