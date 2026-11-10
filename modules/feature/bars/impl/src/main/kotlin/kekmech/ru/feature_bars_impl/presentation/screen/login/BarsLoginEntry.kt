package kekmech.ru.feature_bars_impl.presentation.screen.login

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import kekmech.ru.feature_bars_impl.navigation.BarsLoginScreenKey
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
internal fun BarsLoginEntry(key: BarsLoginScreenKey) {
    val viewModel = koinViewModel<BarsLoginViewModel> { parametersOf(key) }
    val state by viewModel.state.collectAsState()
    BarsLoginScreen(state, viewModel)
}