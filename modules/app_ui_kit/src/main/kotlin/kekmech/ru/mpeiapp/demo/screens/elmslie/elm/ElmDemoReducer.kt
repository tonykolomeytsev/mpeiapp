package kekmech.ru.mpeiapp.demo.screens.elmslie.elm

import kekmech.ru.lib_elm.Resource
import kekmech.ru.mpeiapp.demo.screens.elmslie.elm.ElmDemoEvent.Internal
import kekmech.ru.mpeiapp.demo.screens.elmslie.elm.ElmDemoEvent.Ui
import vivid.money.elmslie.core.store.dsl_reducer.ScreenDslReducer
import kekmech.ru.mpeiapp.demo.screens.elmslie.elm.ElmDemoCommand as Command
import kekmech.ru.mpeiapp.demo.screens.elmslie.elm.ElmDemoEffect as Effect
import kekmech.ru.mpeiapp.demo.screens.elmslie.elm.ElmDemoEvent as Event
import kekmech.ru.mpeiapp.demo.screens.elmslie.elm.ElmDemoState as State

internal class ElmDemoReducer :
    ScreenDslReducer<Event, Ui, Internal, State, Effect, Command>(
        uiEventClass = Ui::class,
        internalEventClass = Internal::class,
    ) {

    override fun Result.internal(event: Internal) =
        when (event) {
            is Internal.LoadNewResourceSuccess ->
                state { copy(resource = Resource.Data(event.string)) }
            is Internal.LoadNewResourceFailure ->
                state { copy(resource = Resource.Error(event.error)) }
        }

    override fun Result.ui(event: Ui) =
        when (event) {
            is Ui.Init -> {
                commands { +Command.LoadNewResource }
            }
            is Ui.Click.TextButton -> {
                state { copy(resource = Resource.Loading) }
                commands { +Command.LoadNewResource }
            }
        }
}
