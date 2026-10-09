package kekmech.ru.feature_schedule_impl.presentation.screen.main.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kekmech.ru.ext_kotlin.moscowLocalDate
import kekmech.ru.feature_schedule_api.domain.model.Classes
import kekmech.ru.feature_schedule_api.domain.model.ClassesType
import kekmech.ru.feature_schedule_api.domain.model.Day
import kekmech.ru.feature_schedule_api.domain.model.WeekOfSemester
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

    // UI state for search and filters
    var searchQuery by rememberSaveable { mutableStateOf("") }
    var selectedClassesType by rememberSaveable { mutableStateOf<ClassesType?>(null) }
    var selectedDiscipline by rememberSaveable { mutableStateOf<String?>(null) }
    var isWeeklyGridMode by rememberSaveable { mutableStateOf(false) }

    // Collect all classes for the active week
    val rawDays = state.selectedSchedule?.weeks?.firstOrNull()?.days.orEmpty()
    val allWeekClasses = remember(rawDays) {
        rawDays.flatMap { it.classes }
    }

    // Extract unique disciplines for quick filter chips
    val availableDisciplines = remember(allWeekClasses) {
        allWeekClasses
            .map { it.name.trim() }
            .filter { it.isNotBlank() }
            .distinct()
            .sorted()
    }

    // Filter predicate based on query, type and selected discipline
    val filterPredicate = remember<(Classes) -> Boolean>(
        searchQuery,
        selectedClassesType,
        selectedDiscipline,
    ) {
        { cls: Classes ->
            val matchesType = selectedClassesType == null || cls.type == selectedClassesType
            val matchesDiscipline = when {
                selectedDiscipline != null -> cls.name.equals(selectedDiscipline, ignoreCase = true)
                searchQuery.isNotBlank() -> {
                    val q = searchQuery.trim()
                    cls.name.contains(q, ignoreCase = true) ||
                        cls.person.contains(q, ignoreCase = true) ||
                        cls.place.contains(q, ignoreCase = true)
                }
                else -> true
            }
            matchesType && matchesDiscipline
        }
    }

    val isFilterActive = searchQuery.isNotBlank() || selectedClassesType != null || selectedDiscipline != null

    // Filtered classes grouped by day for the week
    val daysWithFilteredClasses = remember(rawDays, monday, filterPredicate) {
        (0..6).map { dayOffset ->
            val date = monday.plusDays(dayOffset.toLong())
            val day = rawDays.find { it.date == date || it.dayOfWeek == date.dayOfWeek.value }
                ?: Day(dayOfWeek = date.dayOfWeek.value, date = date, classes = emptyList())
            day to day.classes.filter(filterPredicate)
        }
    }

    val classesCountByDate = remember(daysWithFilteredClasses) {
        daysWithFilteredClasses.associate { it.first.date to it.second.size }
    }

    val totalMatchingCount = remember(daysWithFilteredClasses) {
        daysWithFilteredClasses.sumOf { it.second.size }
    }

    // Classes for the currently selected day
    val selectedDayClasses = remember(daysWithFilteredClasses, state.selectedDate) {
        daysWithFilteredClasses.find { it.first.date == state.selectedDate }?.second
    }

    val weekSubtitle = when (val weekOfSemester = state.weekOfSemester) {
        is WeekOfSemester.Studying -> "Учебная неделя ${weekOfSemester.num}"
        else -> if (state.weekOffset % 2 == 0) "Чётная неделя" else "Нечётная неделя"
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = MpeixTheme.palette.background,
        topBar = {
            val titleText = state.selectedSchedule?.name?.let { "$it • $weekSubtitle" } ?: "Расписание"
            TopAppBar(
                title = titleText,
                actions = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(2.dp),
                    ) {
                        // Toggle view mode: Day Rail vs Full Week Grid
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .background(
                                    if (isWeeklyGridMode) MpeixTheme.palette.primary.copy(alpha = 0.15f)
                                    else MpeixTheme.palette.surfacePlus2
                                )
                                .border(
                                    width = 1.dp,
                                    color = if (isWeeklyGridMode) MpeixTheme.palette.primary
                                    else MpeixTheme.palette.outline.copy(alpha = 0.15f),
                                    shape = RoundedCornerShape(8.dp),
                                )
                                .clickable { isWeeklyGridMode = !isWeeklyGridMode }
                                .padding(horizontal = 8.dp, vertical = 5.dp),
                        ) {
                            Text(
                                text = if (isWeeklyGridMode) "Вся неделя ▦" else "По дням ◫",
                                style = MpeixTheme.typography.paragraphNormal.copy(
                                    fontWeight = FontWeight.SemiBold,
                                    color = if (isWeeklyGridMode) MpeixTheme.palette.primary else MpeixTheme.palette.content,
                                    fontSize = 11.sp,
                                ),
                            )
                        }

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

                        // Week offset indicator
                        if (!state.isOnCurrentWeek) {
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(MpeixTheme.palette.primary.copy(alpha = 0.12f))
                                    .clickable { onAccept(ScheduleEvent.Ui.Action.SelectWeek(0)) }
                                    .padding(horizontal = 7.dp, vertical = 4.dp),
                            ) {
                                Text(
                                    text = if (state.weekOffset > 0) "+${state.weekOffset}" else "${state.weekOffset}",
                                    style = MpeixTheme.typography.labelNormal.copy(
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
                    onClick = {
                        if (!state.isOnCurrentWeek) {
                            onAccept(ScheduleEvent.Ui.Action.SelectWeek(0))
                        }
                        onAccept(ScheduleEvent.Ui.Click.Day(today))
                    },
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
                            style = MpeixTheme.typography.labelBig.copy(
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
            // Top Filter & Search Bar: Subject search, Type filter, Quick Discipline chips
            ScheduleFilterBar(
                searchQuery = searchQuery,
                onSearchQueryChange = { searchQuery = it },
                selectedClassesType = selectedClassesType,
                onClassesTypeSelect = { selectedClassesType = it },
                selectedDiscipline = selectedDiscipline,
                onDisciplineSelect = { selectedDiscipline = it },
                availableDisciplines = availableDisciplines,
                matchingClassesCount = if (isFilterActive) totalMatchingCount else null,
            )

            Spacer(modifier = Modifier.height(4.dp))

            // Schedule Grid: Days on Left, Classes on Right
            if (isWeeklyGridMode) {
                // Full week timeline grid view
                WeeklyTimelineGrid(
                    daysWithClasses = daysWithFilteredClasses,
                    onClassesClick = { cls ->
                        onAccept(ScheduleEvent.Ui.Click.Classes(cls))
                    },
                    onDayHeaderClick = { date ->
                        onAccept(ScheduleEvent.Ui.Click.Day(date))
                        isWeeklyGridMode = false
                    },
                    modifier = Modifier.weight(1f),
                )
            } else {
                // Side-rail view: Days on left rail, selected day classes on right
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                ) {
                    // Left column: Days rail
                    DayRailSelector(
                        firstDayOfWeek = monday,
                        selectedDate = state.selectedDate,
                        classesCountByDate = classesCountByDate,
                        onDayClick = { date ->
                            onAccept(ScheduleEvent.Ui.Click.Day(date))
                        },
                    )

                    // Right column: Classes for selected day
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
                        isFilterActive = isFilterActive,
                        modifier = Modifier.weight(1f),
                    )
                }
            }
        }
    }
}
