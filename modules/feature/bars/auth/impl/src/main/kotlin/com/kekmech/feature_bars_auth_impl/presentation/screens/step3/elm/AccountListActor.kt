package com.kekmech.feature_bars_auth_impl.presentation.screens.step3.elm

import kotlinx.coroutines.flow.Flow
import money.vivid.elmslie.core.store.Actor
import com.kekmech.feature_bars_auth_impl.presentation.screens.step3.elm.AccountListCommand as Command
import com.kekmech.feature_bars_auth_impl.presentation.screens.step3.elm.AccountListEvent as Event

internal class AccountListActor : Actor<Command, Event>() {
    override fun execute(command: Command): Flow<Event> {
        TODO()
    }
}