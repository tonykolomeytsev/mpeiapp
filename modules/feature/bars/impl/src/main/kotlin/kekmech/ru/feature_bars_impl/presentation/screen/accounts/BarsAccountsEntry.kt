package kekmech.ru.feature_bars_impl.presentation.screen.accounts

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import org.koin.compose.viewmodel.koinViewModel

@Composable
internal fun BarsAccountsEntry() {
    val viewModel = koinViewModel<BarsAccountsViewModel>()
    val state by viewModel.state.collectAsState()
    BarsAccountsScreen(
        state = state,
        actions = viewModel,
    )
}
