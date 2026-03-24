package com.kekmech.feature_bars_auth_impl.presentation.screens.main.elm

import kotlinx.coroutines.flow.Flow
import money.vivid.elmslie.core.store.Actor
import com.kekmech.feature_bars_auth_impl.presentation.screens.main.elm.BarsAuthMainCommand as Command
import com.kekmech.feature_bars_auth_impl.presentation.screens.main.elm.BarsAuthMainEvent as Event

internal class BarsAuthMainActor : Actor<Command, Event>() {

    override fun execute(command: Command): Flow<Event> {
        TODO("Not yet implemented")
    }
}