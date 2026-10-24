package kekmech.ru.feature_bars_impl.presentation.screen.main_compose

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.res.stringResource
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
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
import kekmech.ru.feature_bars_impl.presentation.screen.main_compose.compose.DisciplineUiItem
import kekmech.ru.feature_bars_impl.presentation.screen.main_compose.compose.LoginPrompt
import kekmech.ru.feature_bars_impl.presentation.screen.main_compose.compose.UserHeader
import kekmech.ru.feature_bars_impl.presentation.screen.main_compose.elm.BarsComposeEffect
import kekmech.ru.feature_bars_impl.presentation.screen.main_compose.elm.BarsComposeEvent
import kekmech.ru.feature_bars_impl.presentation.screen.main_compose.elm.BarsComposeEvent.Ui
import kekmech.ru.feature_bars_impl.presentation.screen.main_compose.elm.BarsComposeState
import kekmech.ru.feature_bars_impl.presentation.screen.main_compose.elm.BarsTab
import kekmech.ru.feature_bars_impl.presentation.screen.main_compose.summary.SummaryCardSkeleton
import kekmech.ru.feature_bars_impl.presentation.screen.main_compose.summary.SummaryDisciplineUi
import kekmech.ru.feature_bars_impl.presentation.screen.main_compose.summary.SummaryError
import kekmech.ru.feature_bars_impl.presentation.screen.main_compose.summary.cardModifier
import kekmech.ru.feature_bars_impl.presentation.screen.main_compose.summary.summaryItems
import kekmech.ru.feature_bars_impl.presentation.screen.main_compose.summary.toSummary
import kekmech.ru.lib_elm.Resource
import kekmech.ru.ui_kit_segmented.SegmentedControl
import kekmech.ru.ui_theme.theme.MpeixTheme
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import kekmech.ru.res_strings.R.string as Strings

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
    // КМ statuses depend on the current week, so the summary is rebuilt when either input changes
    val summary = remember(state.disciplines, state.currentWeek) {
        state.disciplines.toSummary(state.currentWeek)
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
                if (!state.isLoginRequired) {
                    item(key = "tabs") {
                        BarsTabs(
                            selected = state.selectedTab,
                            onSelect = { onAccept(Ui.Click.Tab(it)) },
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                        )
                    }
                    when (state.selectedTab) {
                        BarsTab.DISCIPLINES -> disciplineItems(state, onAccept)
                        BarsTab.SUMMARY -> summaryItems(
                            summary = summary,
                            expanded = state.expandedSummary,
                            onToggle = { onAccept(Ui.Click.SummaryDiscipline(it)) },
                            onRetry = { onAccept(Ui.Action.PullToRefresh) },
                        )
                    }
                }
            }
        }
    }
}

private fun LazyListScope.disciplineItems(
    state: BarsComposeState,
    onAccept: (BarsComposeEvent) -> Unit,
) {
    when (val disciplines = state.disciplines) {
        is Resource.Data -> items(disciplines.value) {
            DisciplineItem(
                discipline = it,
                onClick = { onAccept(Ui.Click.Discipline(it)) },
                modifier = Modifier
                    .padding(horizontal = 8.dp)
                    .padding(bottom = 8.dp)
            )
        }

        is Resource.Loading -> items(DISCIPLINES_SKELETON_COUNT) {
            SummaryCardSkeleton(Modifier.cardModifier())
        }

        is Resource.Error -> item(key = "disciplines_error") {
            SummaryError(
                throwable = disciplines.error,
                onRetry = { onAccept(Ui.Action.PullToRefresh) },
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 24.dp),
            )
        }
    }
}

private fun Resource<List<DisciplineUiItem>>.toSummary(currentWeek: Int?): Resource<List<SummaryDisciplineUi>> =
    when (this) {
        is Resource.Data -> Resource.Data(value.map { it.discipline }.toSummary(currentWeek))
        is Resource.Loading -> Resource.Loading
        is Resource.Error -> Resource.Error(error)
    }

@Composable
private fun BarsTabs(
    selected: BarsTab,
    onSelect: (BarsTab) -> Unit,
    modifier: Modifier = Modifier,
) {
    SegmentedControl(
        options = BarsTab.entries,
        selected = selected,
        onSelect = onSelect,
        label = { tab ->
            stringResource(
                when (tab) {
                    BarsTab.DISCIPLINES -> Strings.bars_tab_disciplines
                    BarsTab.SUMMARY -> Strings.bars_tab_summary
                }
            )
        },
        modifier = modifier,
    )
}

private const val DISCIPLINES_SKELETON_COUNT = 4

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