package kekmech.ru.feature_schedule_impl.presentation.screen.main.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kekmech.ru.ext_kotlin.moscowLocalDate
import kekmech.ru.feature_schedule_api.domain.model.WeekOfSemester
import kekmech.ru.feature_schedule_impl.R
import kekmech.ru.feature_schedule_impl.presentation.screen.main.elm.ScheduleEffect
import kekmech.ru.feature_schedule_impl.presentation.screen.main.elm.ScheduleEvent
import kekmech.ru.feature_schedule_impl.presentation.screen.main.elm.ScheduleState
import kekmech.ru.lib_schedule.utils.atStartOfWeek
import kekmech.ru.ui_kit_topappbar.TopAppBar
import kekmech.ru.ui_theme.theme.MpeixTheme
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

@Composable
internal fun ScheduleComposeScreen(
    onAccept: (ScheduleEvent) -> Unit,
    state: ScheduleState,
    effects: Flow<ScheduleEffect>,
    modifier: Modifier = Modifier,
) {
    LaunchedEffect(Unit) {
        onAccept(ScheduleEvent.Ui.Init)
    }

    val today = moscowLocalDate()
    val monday = today.atStartOfWeek().plusWeeks(state.weekOffset.toLong())

    val selectedDayClasses = state.selectedSchedule?.weeks?.firstOrNull()?.days
        ?.find { it.date == state.selectedDate || it.dayOfWeek == state.selectedDate.dayOfWeek.value }
        ?.classes

    val weekSubtitle = when (val weekOfSemester = state.weekOfSemester) {
        is WeekOfSemester.Studying -> "Учебная неделя ${weekOfSemester.num}"
        else -> if (state.weekOffset % 2 == 0) "Чётная неделя" else "Нечётная неделя"
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = MpeixTheme.palette.background,
        topBar = {
            TopAppBar(
                title = state.selectedSchedule?.name ?: "Расписание",
                subtitle = weekSubtitle,
                actions = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        // Previous week
                        IconButton(
                            onClick = { onAccept(ScheduleEvent.Ui.Action.SelectWeek(state.weekOffset - 1)) },
                        ) {
                            Text(
                                text = "◀",
                                style = MpeixTheme.typography.header4.copy(
                                    color = MpeixTheme.palette.content,
                                ),
                            )
                        }

                        // Current week indicator / reset
                        if (!state.isOnCurrentWeek) {
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(MpeixTheme.palette.primary.copy(alpha = 0.12f))
                                    .padding(horizontal = 8.dp, vertical = 4.dp),
                            ) {
                                Text(
                                    text = if (state.weekOffset > 0) "+${state.weekOffset}" else "${state.weekOffset}",
                                    style = MpeixTheme.typography.paragraphSmall.copy(
                                        color = MpeixTheme.palette.primary,
                                        fontWeight = FontWeight.Bold,
                                    ),
                                )
                            }
                        }

                        // Next week
                        IconButton(
                            onClick = { onAccept(ScheduleEvent.Ui.Action.SelectWeek(state.weekOffset + 1)) },
                        ) {
                            Text(
                                text = "▶",
                                style = MpeixTheme.typography.header4.copy(
                                    color = MpeixTheme.palette.content,
                                ),
                            )
                        }
                    }
                },
            )
        },
        floatingActionButton = {
            // Show "Сегодня" button if looking at other day or week
            val isNotToday = state.selectedDate != today || !state.isOnCurrentWeek
            if (isNotToday && state.isNavigationFabVisible) {
                FloatingActionButton(
                    onClick = { onAccept(ScheduleEvent.Ui.Click.FAB) },
                    containerColor = MpeixTheme.palette.primary,
                    contentColor = MpeixTheme.palette.contentAccent,
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier.padding(bottom = 60.dp),
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Text(
                            text = "Сегодня",
                            style = MpeixTheme.typography.buttonNormal.copy(
                                color = MpeixTheme.palette.contentAccent,
                                fontWeight = FontWeight.Bold,
                            ),
                        )
                    }
                }
            }
        },
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
        ) {
            // Weekday selector: ПН - СБ
            WeekDaySelector(
                firstDayOfWeek = monday,
                selectedDate = state.selectedDate,
                onDayClick = { date ->
                    onAccept(ScheduleEvent.Ui.Click.Day(date))
                },
            )

            // Classes list for chosen day
            DayScheduleContent(
                classesList = selectedDayClasses,
                isLoading = state.selectedSchedule == null && state.loadingError == null,
                errorMessage = state.loadingError?.localizedMessage,
                onClassesClick = { cls ->
                    onAccept(ScheduleEvent.Ui.Click.Classes(cls))
                },
                onReloadClick = {
                    onAccept(ScheduleEvent.Ui.Click.Reload)
                },
                modifier = Modifier.weight(1f),
            )
        }
    }
}
