package com.kekmech.feature_bars_auth_impl.presentation.screens.step3

import androidx.compose.runtime.Composable
import com.kekmech.feature_bars_auth_impl.presentation.screens.step3.elm.AccountListStoreFactory
import kekmech.ru.lib_elm_compose.ElmContent
import kotlinx.coroutines.flow.Flow
import org.koin.compose.koinInject
import com.kekmech.feature_bars_auth_impl.presentation.screens.step3.elm.AccountListEffect as Effect
import com.kekmech.feature_bars_auth_impl.presentation.screens.step3.elm.AccountListEvent as Event
import com.kekmech.feature_bars_auth_impl.presentation.screens.step3.elm.AccountListState as State

@Composable
internal fun AccountListScreen() {
    val factory = koinInject<AccountListStoreFactory>()
    ElmContent(
        key = "AccountListScreen",
        storeFactory = { factory.create() },
        content = ::AccountListScreen,
    )
}

@Composable
private fun AccountListScreen(
    onAccept: (Event) -> Unit,
    state: State,
    effects: Flow<Effect>,
) {
    TODO()
}