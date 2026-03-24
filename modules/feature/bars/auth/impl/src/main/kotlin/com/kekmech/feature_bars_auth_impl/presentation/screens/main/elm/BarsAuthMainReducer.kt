package com.kekmech.feature_bars_auth_impl.presentation.screens.main.elm

import com.kekmech.feature_bars_auth_impl.presentation.screens.main.elm.BarsAuthMainEvent.Internal
import com.kekmech.feature_bars_auth_impl.presentation.screens.main.elm.BarsAuthMainEvent.Ui
import money.vivid.elmslie.core.store.ScreenReducer
import money.vivid.elmslie.core.store.StateReducer
import com.kekmech.feature_bars_auth_impl.presentation.screens.main.elm.BarsAuthMainCommand as Command
import com.kekmech.feature_bars_auth_impl.presentation.screens.main.elm.BarsAuthMainEffect as Effect
import com.kekmech.feature_bars_auth_impl.presentation.screens.main.elm.BarsAuthMainEvent as Event
import com.kekmech.feature_bars_auth_impl.presentation.screens.main.elm.BarsAuthMainState as State

internal class BarsAuthMainReducer : ScreenReducer<Event, Ui, Internal, State, Effect, Command>(
    uiEventClass = Ui::class,
    internalEventClass = Internal::class,
) {

    override fun Result.internal(event: Internal) {
        TODO("Not yet implemented")
    }

    override fun Result.ui(event: Ui) {
        TODO("Not yet implemented")
    }
}