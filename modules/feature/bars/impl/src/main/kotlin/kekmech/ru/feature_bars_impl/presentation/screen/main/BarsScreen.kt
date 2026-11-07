package kekmech.ru.feature_bars_impl.presentation.screen.main

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Scaffold
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.PullToRefreshDefaults
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import kekmech.ru.feature_bars_impl.presentation.screen.main.compose.DisciplineItem
import kekmech.ru.feature_bars_impl.presentation.screen.main.compose.DisciplinesShimmer
import kekmech.ru.feature_bars_impl.presentation.screen.main.compose.LoginPrompt
import kekmech.ru.feature_bars_impl.presentation.screen.main.compose.ProfileTopBar
import kekmech.ru.feature_bars_impl.presentation.screen.main.compose.ShowingFromCacheBanner
import kekmech.ru.feature_bars_impl.presentation.screen.main.elm.BarsEffect
import kekmech.ru.feature_bars_impl.presentation.screen.main.elm.BarsEvent
import kekmech.ru.feature_bars_impl.presentation.screen.main.elm.BarsEvent.Ui
import kekmech.ru.feature_bars_impl.presentation.screen.main.elm.BarsState
import kekmech.ru.lib_elm.Resource
import kekmech.ru.ui_kit_lists.SectionHeader
import kekmech.ru.ui_kit_states.EmptyState
import kekmech.ru.ui_theme.layout.adaptiveContentWidth
import kotlinx.coroutines.flow.Flow
import kekmech.ru.res_strings.R.string as Strings

@Composable
internal fun BarsComposeScreen(
    onAccept: (BarsEvent) -> Unit,
    state: BarsState,
    @Suppress("Unused")
    effects: Flow<BarsEffect>,
) {
    val layoutDirection = LocalLayoutDirection.current
    val pullToRefreshState = rememberPullToRefreshState()
    Scaffold(
        modifier = Modifier.fillMaxSize(),
    ) { innerPadding ->
        PullToRefreshBox(
            isRefreshing = state.isLoading,
            onRefresh = {
                onAccept(Ui.Action.PullToRefresh)
            },
            state = pullToRefreshState,
            indicator = {
                PullToRefreshDefaults.Indicator(
                    state = pullToRefreshState,
                    isRefreshing = state.isLoading,
                    modifier = Modifier
                        .align(Alignment.TopCenter)
                        .padding(top = innerPadding.calculateTopPadding()),
                )
            },
            modifier = Modifier.fillMaxSize(),
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .adaptiveContentWidth(),
                contentPadding = PaddingValues(
                    start = innerPadding.calculateStartPadding(layoutDirection),
                    top = innerPadding.calculateTopPadding(),
                    end = innerPadding.calculateEndPadding(layoutDirection),
                    bottom = innerPadding.calculateBottomPadding() + 80.dp,
                ),
            ) {
                item {
                    ProfileTopBar(
                        name = state.profile.value?.name,
                        group = state.profile.value?.group,
                        isLoading = state.profile.isLoading && !state.isLoginRequired,
                        onProfileClick = { onAccept(Ui.Click.Profile) },
                        onSettingsClick = { onAccept(Ui.Click.Settings) },
                        modifier = Modifier.padding(bottom = 8.dp),
                    )
                }
                if (state.showingFromCacheReasons.isNotEmpty() && !state.isLoginRequired) {
                    item {
                        ShowingFromCacheBanner(
                            reasons = state.showingFromCacheReasons,
                            modifier = Modifier.padding(bottom = 8.dp),
                        )
                    }
                }
                if (state.isLoginRequired) {
                    item {
                        LoginPrompt(
                            onLoginClick = { onAccept(Ui.Click.Login) }
                        )
                    }
                }
                when {
                    // The login prompt is the whole content, there is nothing to load
                    state.isLoginRequired -> Unit

                    state.disciplines is Resource.Loading -> item {
                        DisciplinesShimmer()
                    }

                    state.disciplines is Resource.Data -> {
                        item {
                            SectionHeader(
                                title = stringResource(Strings.bars_disciplines_header),
                                subtitle = state.semesterName,
                                modifier = Modifier.padding(bottom = 8.dp)
                            )
                        }
                        if (state.disciplines.value.isEmpty()) {
                            item {
                                EmptyState(
                                    title = stringResource(Strings.bars_disciplines_empty_title),
                                    subtitle = stringResource(Strings.bars_disciplines_empty_subtitle),
                                )
                            }
                        }
                        items(state.disciplines.value) {
                            DisciplineItem(
                                discipline = it,
                                onClick = { onAccept(Ui.Click.Discipline(it)) },
                                modifier = Modifier
                                    .padding(horizontal = 16.dp)
                                    .padding(bottom = 8.dp)
                            )
                        }
                    }

                    else -> Unit
                }
            }
        }
    }
}
