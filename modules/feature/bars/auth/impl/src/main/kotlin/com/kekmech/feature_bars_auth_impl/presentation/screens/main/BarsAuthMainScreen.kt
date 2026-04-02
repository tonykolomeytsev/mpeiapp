package com.kekmech.feature_bars_auth_impl.presentation.screens.main

import androidx.compose.runtime.Composable
import com.kekmech.feature_bars_auth_impl.presentation.screens.error.ErrorScreen
import com.kekmech.feature_bars_auth_impl.presentation.screens.loading.LoadingScreen
import com.kekmech.feature_bars_auth_impl.presentation.screens.main.elm.AuthStage
import com.kekmech.feature_bars_auth_impl.presentation.screens.step1.LoginPasswordScreen
import com.kekmech.feature_bars_auth_impl.presentation.screens.step2.TwoFactorScreen
import kotlinx.coroutines.flow.Flow
import com.kekmech.feature_bars_auth_impl.presentation.screens.main.elm.BarsAuthMainEffect as Effect
import com.kekmech.feature_bars_auth_impl.presentation.screens.main.elm.BarsAuthMainEvent as Event
import com.kekmech.feature_bars_auth_impl.presentation.screens.main.elm.BarsAuthMainState as State

@Composable
internal fun BarsAuthMainScreen(
    @Suppress("Unused")
    onAccept: (Event) -> Unit,
    state: State,
    @Suppress("Unused")
    effects: Flow<Effect>,
) {
    when (state.stage) {
        AuthStage.LoginPassword -> LoginPasswordScreen()
        AuthStage.TwoFactor -> TwoFactorScreen()
        AuthStage.IrrecoverableError -> ErrorScreen()
        else -> LoadingScreen()
    }
}
