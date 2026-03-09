package kekmech.ru.feature_bars_impl.presentation.screen.main_compose

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Scaffold
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kekmech.ru.feature_bars_impl.presentation.screen.main_compose.compose.DisciplineItem
import kekmech.ru.feature_bars_impl.presentation.screen.main_compose.compose.LoginPrompt
import kekmech.ru.feature_bars_impl.presentation.screen.main_compose.compose.UserHeader
import kekmech.ru.feature_bars_impl.presentation.screen.main_compose.elm.BarsComposeEffect
import kekmech.ru.feature_bars_impl.presentation.screen.main_compose.elm.BarsComposeEvent
import kekmech.ru.feature_bars_impl.presentation.screen.main_compose.elm.BarsComposeEvent.Ui
import kekmech.ru.feature_bars_impl.presentation.screen.main_compose.elm.BarsComposeState
import kekmech.ru.lib_elm.Resource
import kekmech.ru.ui_theme.theme.MpeixTheme
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow

@Composable
internal fun BarsComposeScreen(
    onAccept: (BarsComposeEvent) -> Unit,
    state: BarsComposeState,
    @Suppress("Unused")
    effects: Flow<BarsComposeEffect>,
) {
    var isRefreshing by remember { mutableStateOf(false) }
    LaunchedEffect(isRefreshing) {
        delay(3000)
        isRefreshing = false
    }
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = MpeixTheme.palette.background,
        contentColor = MpeixTheme.palette.content,
    ) { innerPadding ->
        PullToRefreshBox(
            isRefreshing = isRefreshing,
            onRefresh = {
                isRefreshing = true
                onAccept(Ui.Action.PullToRefresh)
            },
            modifier = Modifier.fillMaxSize()
        ) {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = innerPadding,
            ) {
                item {
                    UserHeader(
                        name = state.profile.value?.name,
                        group = state.profile.value?.group,
                        isLoading = state.profile.isLoading && !state.isLoginRequired,
                        onSettingsClick = { onAccept(Ui.Click.Settings) },
                    )
                }
                if (state.isLoginRequired) {
                    item {
                        LoginPrompt(
                            onLoginClick = { onAccept(Ui.Click.Login) }
                        )
                    }
                }
                when (state.disciplines) {
                    is Resource.Data -> items(state.disciplines.value) {
                        DisciplineItem(
                            discipline = it,
                            onClick = { onAccept(Ui.Click.Discipline(it)) },
                            modifier = Modifier
                                .padding(horizontal = 8.dp)
                                .padding(bottom = 8.dp)
                        )
                    }

                    else -> Unit
                }
            }
        }
    }
}

@Preview
@Composable
private fun BarsComposeScreenPreview() {
    MpeixTheme {
        BarsComposeScreen(
            onAccept = { /* no-op */ },
            state = BarsComposeState(),
            effects = emptyFlow(),
        )
    }
}