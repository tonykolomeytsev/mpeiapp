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
import com.kekmech.feature_bars_grades_api.Grade
import kekmech.ru.feature_bars_impl.presentation.screen.main_compose.compose.DisciplineItem
import kekmech.ru.feature_bars_impl.presentation.screen.main_compose.compose.LegacyDisciplineItem
import kekmech.ru.feature_bars_impl.presentation.screen.main_compose.compose.LoginPrompt
import kekmech.ru.feature_bars_impl.presentation.screen.main_compose.compose.ProfileMenu
import kekmech.ru.feature_bars_impl.presentation.screen.main_compose.compose.UserHeader
import kekmech.ru.feature_bars_impl.presentation.screen.main_compose.elm.BarsComposeEffect
import kekmech.ru.feature_bars_impl.presentation.screen.main_compose.elm.BarsComposeEvent
import kekmech.ru.feature_bars_impl.presentation.screen.main_compose.elm.BarsComposeEvent.Ui
import kekmech.ru.feature_bars_impl.presentation.screen.main_compose.elm.BarsComposeState
import kekmech.ru.feature_bars_impl.presentation.screen.main_compose.elm.BarsProfile
import kekmech.ru.lib_elm.Resource
import kekmech.ru.lib_elm.toResource
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
                        name = state.profile.value?.name ?: "Антон Коломейцев",
                        group = state.profile.value?.group ?: "С-12-16",
                        isLoading = false, // state.profile.isLoading && !state.isLoginRequired,
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
                            item = it,
                            onClick = { /* no-op */ },
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
            state = BarsComposeState(
                profile = BarsProfile(name = "Антон Коломейцев", group = "C-12-16").toResource(),
                disciplines = listOf(
                    DisciplineItem(
                        title = "Статистическая динамика автоматических систем",
                        teacher = "Меркурьев И.В.",
                        assessmentType = "Экзамен",
                        controlActivities = listOf(
                            "Определение математического ожидания, дисперсии и корреляционной функции на входе и выходе системы автоматического управления" to
                                    Grade(5.0f),
                            "Статистический анализ системы автоматического управления в частотной области" to
                                    Grade(4.0f),
                            "Методы оптимальной фильтрации" to
                                    Grade(3.0f),
                            "Оценка точности автоматической системы управления при случайных воздействиях" to
                                    Grade(2.0f),
                        ),
                        intermediate = Grade(5f),
                        final = Grade(4f),
                    ),
                    DisciplineItem(
                        title = "Статистическая динамика автоматических систем",
                        teacher = "Меркурьев И.В.",
                        assessmentType = "Экзамен",
                        controlActivities = listOf(
                            "Определение математического ожидания, дисперсии и корреляционной функции на входе и выходе системы автоматического управления" to
                                    Grade(5.0f),
                            "Статистический анализ системы автоматического управления в частотной области" to
                                    Grade(4.0f),
                            "Методы оптимальной фильтрации" to
                                    null,
                            "Оценка точности автоматической системы управления при случайных воздействиях" to
                                    null,
                        ),
                        intermediate = null,
                        final = null,
                    )
                ).toResource()
            ),
            effects = emptyFlow(),
        )
    }
}