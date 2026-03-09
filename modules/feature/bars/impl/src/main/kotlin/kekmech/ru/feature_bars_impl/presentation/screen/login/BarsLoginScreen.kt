package kekmech.ru.feature_bars_impl.presentation.screen.login

import androidx.compose.runtime.Composable
import kekmech.ru.feature_bars_impl.presentation.screen.login.compose.BarsAccountSelectionScreen
import kekmech.ru.feature_bars_impl.presentation.screen.login.compose.BarsLoginPasswordScreen
import kekmech.ru.feature_bars_impl.presentation.screen.login.compose.BarsTwoFactorCodeScreen
import kekmech.ru.feature_bars_impl.presentation.screen.login.elm.BarsLoginEffect
import kekmech.ru.feature_bars_impl.presentation.screen.login.elm.BarsLoginEvent
import kekmech.ru.feature_bars_impl.presentation.screen.login.elm.BarsLoginStage
import kekmech.ru.feature_bars_impl.presentation.screen.login.elm.BarsLoginState
import kotlinx.coroutines.flow.Flow

@Composable
internal fun BarsLoginScreen(
    onAccept: (BarsLoginEvent) -> Unit,
    state: BarsLoginState,
    effects: Flow<BarsLoginEffect>,
) {
    when (state.stage) {
        BarsLoginStage.INIT -> Unit
        BarsLoginStage.LOGIN_PASSWORD -> BarsLoginPasswordScreen(
            onAccept = onAccept,
            state = state.loginPasswordState,
            effects = effects,
        )

        BarsLoginStage.TWO_FACTOR_CODE -> BarsTwoFactorCodeScreen(
            onAccept = onAccept,
            state = state.twoFactorCodeState,
            effects = effects,
        )

        BarsLoginStage.ACCOUNT_SELECTION -> BarsAccountSelectionScreen(
            onAccept = onAccept,
            state = state.accountSelectionState,
            effects = effects,
        )
    }
}