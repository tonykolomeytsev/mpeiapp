package com.kekmech.feature_bars_auth_impl.presentation.screens.step1.elm

import com.kekmech.feature_bars_auth_impl.data.AuthRepository
import com.kekmech.feature_bars_auth_impl.domain.UserCredentials
import com.kekmech.feature_bars_auth_impl.presentation.screens.step1.elm.LoginPasswordEvent.Internal
import kekmech.ru.lib_elm.actorFlow
import kotlinx.coroutines.flow.Flow
import money.vivid.elmslie.core.store.Actor
import com.kekmech.feature_bars_auth_impl.presentation.screens.step1.elm.LoginPasswordCommand as Command
import com.kekmech.feature_bars_auth_impl.presentation.screens.step1.elm.LoginPasswordEvent as Event

internal class LoginPasswordActor(
    private val authRepository: AuthRepository,
) : Actor<Command, Event>() {

    override fun execute(command: Command): Flow<Event> = when (command) {
        is Command.LoginWithPassword -> actorFlow {
            authRepository.login(UserCredentials(command.login, command.password)).fold(
                ifLeft = Internal::LoginWithPasswordFailure,
                ifRight = { Internal.LoginWithPasswordSuccess }
            )
        }
    }
}