package com.kekmech.feature_bars_auth_impl.presentation.screens.step1.elm

import kotlinx.coroutines.flow.Flow
import money.vivid.elmslie.core.store.Actor
import com.kekmech.feature_bars_auth_impl.presentation.screens.step1.elm.LoginPasswordCommand as Command
import com.kekmech.feature_bars_auth_impl.presentation.screens.step1.elm.LoginPasswordEvent as Event

internal class LoginPasswordActor : Actor<Command, Event>() {
    override fun execute(command: Command): Flow<Event> {
        TODO()
    }
}