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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import kekmech.ru.feature_bars_impl.domain.AssessedDiscipline
import kekmech.ru.feature_bars_impl.presentation.screen.main.compose.DisciplineItem
import kekmech.ru.feature_bars_impl.presentation.screen.main.compose.DisciplineUiItem
import kekmech.ru.feature_bars_impl.presentation.screen.main.compose.LoginPrompt
import kekmech.ru.feature_bars_impl.presentation.screen.main.compose.ProfileTopBar
import kekmech.ru.feature_bars_impl.presentation.screen.main.elm.BarsEffect
import kekmech.ru.feature_bars_impl.presentation.screen.main.elm.BarsEvent
import kekmech.ru.feature_bars_impl.presentation.screen.main.elm.BarsEvent.Ui
import kekmech.ru.feature_bars_impl.presentation.screen.main.elm.BarsState
import kekmech.ru.feature_bars_impl.presentation.screen.main.elm.BarsProfile
import kekmech.ru.lib_elm.Resource
import kekmech.ru.ui_theme.theme.MpeixTheme
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import kotlin.time.Duration.Companion.milliseconds

@Composable
internal fun BarsComposeScreen(
    onAccept: (BarsEvent) -> Unit,
    state: BarsState,
    @Suppress("Unused")
    effects: Flow<BarsEffect>,
) {
    val layoutDirection = LocalLayoutDirection.current
    var isRefreshing by remember { mutableStateOf(false) }
    LaunchedEffect(isRefreshing) {
        delay(3000.milliseconds)
        isRefreshing = false
    }
    val pullToRefreshState = rememberPullToRefreshState()
    Scaffold(
        modifier = Modifier.fillMaxSize(),
    ) { innerPadding ->
        PullToRefreshBox(
            isRefreshing = isRefreshing,
            onRefresh = {
                isRefreshing = true
                onAccept(Ui.Action.PullToRefresh)
            },
            state = pullToRefreshState,
            indicator = {
                PullToRefreshDefaults.Indicator(
                    state = pullToRefreshState,
                    isRefreshing = isRefreshing,
                    modifier = Modifier
                        .align(Alignment.TopCenter)
                        .padding(top = innerPadding.calculateTopPadding()),
                )
            },
            modifier = Modifier.fillMaxSize()
        ) {
            // Like on the dashboard, the top bar is the first item and scrolls under the status bar
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
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
                                .padding(horizontal = 16.dp)
                                .padding(bottom = 8.dp)
                        )
                    }

                    else -> Unit
                }
            }
        }
    }
}

@PreviewLightDark
@Composable
private fun BarsScreenDataPreview() = BarsScreenPreview(
    BarsState(
        profile = Resource.Data(BarsProfile(name = "Антон Коломейцев", group = "С-12-16")),
        disciplines = Resource.Data(PreviewDisciplines),
    )
)

@Preview
@Composable
private fun BarsScreenLoadingPreview() = BarsScreenPreview(BarsState())

@PreviewLightDark
@Composable
private fun BarsScreenLoginRequiredPreview() = BarsScreenPreview(BarsState(isLoginRequired = true))

@Preview
@Composable
private fun BarsScreenLongNamePreview() = BarsScreenPreview(
    BarsState(
        profile = Resource.Data(BarsProfile(name = "Константинопольская Александра-Мария", group = "ИЭ-07м-25")),
        disciplines = Resource.Data(PreviewDisciplines.take(2)),
    )
)

@Preview(device = Devices.TABLET)
@Composable
private fun BarsScreenTabletPreview() = BarsScreenDataPreview()

@Composable
private fun BarsScreenPreview(state: BarsState) {
    MpeixTheme {
        BarsComposeScreen(
            onAccept = { /* no-op */ },
            state = state,
            effects = emptyFlow(),
        )
    }
}

private val PreviewDisciplines = listOf(
    previewDiscipline("Иностранный язык", "Чеботарева О.А.", "Зачёт с оценкой", finalMark = 4f, 5f, 4f, 4f),
    previewDiscipline("Математический анализ", "Петров А.В.", "Экзамен", finalMark = null, 3f, 4f),
    previewDiscipline("Физика", "Сидорова Е.Н.", "Экзамен", finalMark = 5f, 5f, 5f, 4f, 5f),
    previewDiscipline("Инженерная и компьютерная графика", "Лутошкина Е.Ю.", "Зачёт", finalMark = null),
    previewDiscipline("История России", "Смирнова С.С.", "Зачёт", finalMark = 2f, 2f, 3f),
)

private fun previewDiscipline(
    name: String,
    person: String,
    type: String,
    finalMark: Float?,
    vararg marks: Float,
) = DisciplineUiItem(
    name = name,
    person = person,
    type = type,
    finalMark = finalMark,
    marks = marks.toList(),
    discipline = AssessedDiscipline(
        name = name,
        person = person,
        assessmentType = type,
        controlActivities = emptyList(),
        finalGrades = emptyList(),
    ),
)
