package com.kekmech.feature_bars_auth_impl.presentation.screens.step2.elm

import kotlinx.coroutines.flow.Flow
import money.vivid.elmslie.core.store.Actor
import com.kekmech.feature_bars_auth_impl.presentation.screens.step2.elm.TwoFactorCommand as Command
import com.kekmech.feature_bars_auth_impl.presentation.screens.step2.elm.TwoFactorEvent as Event

internal class TwoFactorActor : Actor<Command, Event>() {
    override fun execute(command: Command): Flow<Event> {
        TODO()
    }
}