package com.kekmech.feature_bars_auth_impl.presentation.screens.step1

import androidx.compose.runtime.Composable
import com.kekmech.feature_bars_auth_impl.presentation.screens.step1.elm.LoginPasswordStoreFactory
import kekmech.ru.lib_elm_compose.ElmContent
import kotlinx.coroutines.flow.Flow
import org.koin.compose.koinInject
import com.kekmech.feature_bars_auth_impl.presentation.screens.step1.elm.LoginPasswordEffect as Effect
import com.kekmech.feature_bars_auth_impl.presentation.screens.step1.elm.LoginPasswordEvent as Event
import com.kekmech.feature_bars_auth_impl.presentation.screens.step1.elm.LoginPasswordState as State

@Composable
internal fun LoginPasswordScreen() {
    val factory = koinInject<LoginPasswordStoreFactory>()
    ElmContent(
        key = "LoginPasswordScreen",
        storeFactory = { factory.create() },
        content = ::LoginPasswordScreen,
    )
}

@Composable
private fun LoginPasswordScreen(
    onAccept: (Event) -> Unit,
    state: State,
    effects: Flow<Effect>,
) {
    TODO()
}