package kekmech.ru.feature_bars_impl.presentation.screen.login

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import org.koin.compose.viewmodel.koinViewModel

@Composable
internal fun BarsLoginEntry() {
    val viewModel = koinViewModel<BarsLoginViewModel>()
    val state by viewModel.state.collectAsState()
    BarsLoginSheet(
        state = state,
        actions = viewModel,
    )
}
