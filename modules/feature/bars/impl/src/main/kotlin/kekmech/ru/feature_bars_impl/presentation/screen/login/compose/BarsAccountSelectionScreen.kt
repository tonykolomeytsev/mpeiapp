package kekmech.ru.feature_bars_impl.presentation.screen.login.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.fastForEach
import kekmech.ru.feature_bars_impl.domain.ExceptionWithId
import kekmech.ru.feature_bars_impl.presentation.screen.login.elm.BarsAccountSelectionState
import kekmech.ru.feature_bars_impl.presentation.screen.login.elm.BarsLoginEffect
import kekmech.ru.feature_bars_impl.presentation.screen.login.elm.BarsLoginEvent
import kekmech.ru.feature_bars_impl.presentation.screen.login.elm.BarsLoginEvent.Ui
import kekmech.ru.ui_kit_buttons.LegacyButton
import kekmech.ru.ui_kit_topappbar.TopAppBar
import kekmech.ru.ui_shimmer.shimmer
import kekmech.ru.ui_theme.theme.MpeixTheme
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow

@Composable
internal fun BarsAccountSelectionScreen(
    onAccept: (BarsLoginEvent) -> Unit,
    state: BarsAccountSelectionState,
    @Suppress("Unused")
    effects: Flow<BarsLoginEffect>,
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = "Выбор аккаунта",
                navigationIcon = {
                    BackIconButton { onAccept(Ui.Click.Back) }
                },
            )
        },
        bottomBar = {
            LegacyButton(
                text = "Продолжить",
                onClick = { onAccept(Ui.Click.SubmitAccountName) },
                enabled = state.selectedAccountId != null,
                loading = state.isLoading,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            )
        },
        containerColor = MpeixTheme.palette.background,
        contentColor = MpeixTheme.palette.content,
        modifier = Modifier
            .navigationBarsPadding()
            .imePadding(),
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(horizontal = 16.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp, Alignment.Top),
        ) {
            if (state.isLoading) {
                repeat(3) {
                    Box(
                        modifier = Modifier
                            .shimmer()
                            .fillMaxWidth()
                            .height(64.dp)
                            .background(MpeixTheme.palette.shimmer, RoundedCornerShape(8.dp))
                    )
                }
            } else {
                state.accounts.fastForEach { account ->
                    AccountItem(
                        account = account,
                        isSelected = account.id == state.selectedAccountId,
                        onClick = { onAccept(Ui.Click.AvailableAccount(account.id)) },
                    )
                }
            }
            if (state.failure != null && !state.isLoading) {
                ErrorBlock(state.failure)
            }
        }
    }
}

@Preview
@Composable
private fun BarsAccountSelectionScreenPreview() {
    MpeixTheme {
        BarsAccountSelectionScreen(
            onAccept = { /* no-op */ },
            state = BarsAccountSelectionState(
                accounts = listOf(
                    AccountItemUi(
                        id = "1",
                        name = "Маиков Д.А.",
                        group = "С-12-21",
                        status = "Завершил обучение",
                    ),
                    AccountItemUi(
                        id = "2",
                        name = "Маиков Д.А.",
                        group = "А-12м-25",
                        status = "Обучается",
                    )
                ),
                selectedAccountId = "2",
                failure = ExceptionWithId(RuntimeException()),
            ),
            effects = emptyFlow(),
        )
    }
}

@Preview
@Composable
private fun BarsAccountSelectionScreenPreview2() {
    MpeixTheme {
        BarsAccountSelectionScreen(
            onAccept = { /* no-op */ },
            state = BarsAccountSelectionState(
                isLoading = true,
            ),
            effects = emptyFlow(),
        )
    }
}
