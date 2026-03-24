package com.kekmech.feature_bars_auth_impl.presentation.screens.step2

import androidx.compose.runtime.Composable
import com.kekmech.feature_bars_auth_impl.presentation.screens.step2.elm.TwoFactorStoreFactory
import kekmech.ru.lib_elm_compose.ElmContent
import kotlinx.coroutines.flow.Flow
import org.koin.compose.koinInject
import com.kekmech.feature_bars_auth_impl.presentation.screens.step2.elm.TwoFactorEffect as Effect
import com.kekmech.feature_bars_auth_impl.presentation.screens.step2.elm.TwoFactorEvent as Event
import com.kekmech.feature_bars_auth_impl.presentation.screens.step2.elm.TwoFactorState as State

@Composable
internal fun TwoFactorScreen() {
    val factory = koinInject<TwoFactorStoreFactory>()
    ElmContent(
        key = "TwoFactorScreen",
        storeFactory = { factory.create() },
        content = ::TwoFactorScreen,
    )
}

@Composable
private fun TwoFactorScreen(
    onAccept: (Event) -> Unit,
    state: State,
    effects: Flow<Effect>,
) {
    TODO()
}