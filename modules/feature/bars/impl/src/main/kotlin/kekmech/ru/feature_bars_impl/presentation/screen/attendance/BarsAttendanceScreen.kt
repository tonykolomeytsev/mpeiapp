package kekmech.ru.feature_bars_impl.presentation.screen.attendance

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.PullToRefreshDefaults
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import io.github.tonykolomeytsev.mpeix.sdk.Absence
import io.github.tonykolomeytsev.mpeix.sdk.AbsenceId
import io.github.tonykolomeytsev.mpeix.sdk.Assessment
import io.github.tonykolomeytsev.mpeix.sdk.Attendance
import io.github.tonykolomeytsev.mpeix.sdk.DiagnosticId
import io.github.tonykolomeytsev.mpeix.sdk.DisciplineAttendance
import io.github.tonykolomeytsev.mpeix.sdk.DisciplineId
import io.github.tonykolomeytsev.mpeix.sdk.LessonKind
import io.github.tonykolomeytsev.mpeix.sdk.SdkError
import io.github.tonykolomeytsev.mpeix.sdk.SiteDate
import io.github.tonykolomeytsev.mpeix.sdk.SiteTime
import kekmech.ru.ext_kotlin.capitalizeSafe
import kekmech.ru.feature_bars_impl.data.sdk.SdkErrorException
import kekmech.ru.feature_bars_impl.presentation.compose.formatPercent
import kekmech.ru.lib_elm.Resource
import kekmech.ru.ui_kit_lists.SectionHeader
import kekmech.ru.ui_kit_states.EmptyState
import kekmech.ru.ui_kit_states.ErrorState
import kekmech.ru.ui_kit_topappbar.topappbar.TopAppBar
import kekmech.ru.ui_shimmer.shimmer
import kekmech.ru.ui_theme.layout.adaptiveContentWidth
import kekmech.ru.ui_theme.theme.MpeixTheme
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale
import kekmech.ru.res_strings.R.string as Strings

private val CardShape = RoundedCornerShape(12.dp)
private val BadgeShape = RoundedCornerShape(4.dp)
private const val SHIMMER_CARDS_COUNT = 3

/** Two sections: the unexcused share per discipline, then every absence, newest first. */
@Composable
internal fun BarsAttendanceScreen(
    state: BarsAttendanceState,
    onBackClick: () -> Unit,
    onRefresh: () -> Unit,
    onReloadDisciplines: () -> Unit,
    onReloadAbsences: () -> Unit,
) {
    val layoutDirection = LocalLayoutDirection.current
    val pullToRefreshState = rememberPullToRefreshState()
    Scaffold(
        topBar = {
            TopAppBar(
                navigationIcon = { BackIconButton(onClick = onBackClick) },
                title = stringResource(Strings.bars_attendance_title),
            )
        },
    ) { innerPadding ->
        PullToRefreshBox(
            isRefreshing = state.isRefreshing,
            onRefresh = onRefresh,
            state = pullToRefreshState,
            indicator = {
                PullToRefreshDefaults.Indicator(
                    state = pullToRefreshState,
                    isRefreshing = state.isRefreshing,
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
                    bottom = innerPadding.calculateBottomPadding() + 24.dp,
                ),
            ) {
                disciplinesSection(state.disciplines, onReloadDisciplines)
                absencesSection(state.absences, onReloadAbsences)
            }
        }
    }
}

private fun LazyListScope.disciplinesSection(
    disciplines: Resource<List<DisciplineAttendance>>,
    onReloadClick: () -> Unit,
) {
    item(key = "disciplines_header") {
        SectionHeader(
            title = stringResource(Strings.bars_attendance_disciplines_header),
            subtitle = stringResource(Strings.bars_attendance_disciplines_subtitle),
            modifier = Modifier.padding(bottom = 8.dp),
        )
    }
    when (disciplines) {
        is Resource.Loading -> item(key = "disciplines_shimmer") { CardsShimmer() }

        is Resource.Error -> item(key = "disciplines_error") {
            ErrorState(
                throwable = disciplines.error,
                onReloadClick = onReloadClick,
                modifier = Modifier.padding(vertical = 16.dp),
            )
        }

        is Resource.Data -> {
            if (disciplines.value.isEmpty()) {
                item(key = "disciplines_empty") {
                    EmptyState(title = stringResource(Strings.bars_attendance_disciplines_empty))
                }
            }
            items(disciplines.value, key = { "discipline_${it.id.value}" }) {
                DisciplineAttendanceItem(
                    discipline = it,
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .padding(bottom = 8.dp),
                )
            }
        }
    }
}

private fun LazyListScope.absencesSection(
    absences: Resource<List<Absence>>,
    onReloadClick: () -> Unit,
) {
    item(key = "absences_header") {
        SectionHeader(
            title = stringResource(Strings.bars_attendance_absences_header),
            modifier = Modifier.padding(top = 16.dp, bottom = 8.dp),
        )
    }
    when (absences) {
        is Resource.Loading -> item(key = "absences_shimmer") { CardsShimmer() }

        is Resource.Error -> item(key = "absences_error") {
            ErrorState(
                throwable = absences.error,
                onReloadClick = onReloadClick,
                modifier = Modifier.padding(vertical = 16.dp),
            )
        }

        is Resource.Data -> {
            if (absences.value.isEmpty()) {
                item(key = "absences_empty") {
                    EmptyState(
                        title = stringResource(Strings.bars_attendance_absences_empty_title),
                        subtitle = stringResource(Strings.bars_attendance_absences_empty_subtitle),
                    )
                }
            }
            items(absences.value, key = { "absence_${it.id.value}" }) {
                AbsenceItem(
                    absence = it,
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .padding(bottom = 8.dp),
                )
            }
        }
    }
}

/** Name and assessment, then the unexcused share the site shows: `unexcused / lessons`. */
@Composable
private fun DisciplineAttendanceItem(
    discipline: DisciplineAttendance,
    modifier: Modifier = Modifier,
) {
    val attendance = discipline.attendance
    val locale = LocalConfiguration.current.locales[0]
    val percent = remember(attendance, locale) {
        formatPercent(attendance.unexcused, attendance.lessons, locale)
    }
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(MpeixTheme.palette.secondary, CardShape)
            .padding(vertical = 12.dp, horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(4.dp),
        ) {
            Text(
                text = discipline.name,
                style = MpeixTheme.typography.bodyLarge,
                color = MpeixTheme.palette.onSurface,
            )
            Text(
                text = discipline.assessment.raw.capitalizeSafe(),
                style = MpeixTheme.typography.bodyMedium,
                color = MpeixTheme.palette.onSurfaceVariant,
            )
        }
        Text(
            text = percent,
            style = MpeixTheme.typography.titleMedium,
            color = if (attendance.unexcused > 0) {
                MpeixTheme.palette.onSurface
            } else {
                MpeixTheme.palette.onSurfaceVariant
            },
        )
    }
}

/** "6 апреля, пн • 4 пара • 15:35–17:10", the discipline, the lesson kind with teachers and the reason badge. */
@Composable
private fun AbsenceItem(
    absence: Absence,
    modifier: Modifier = Modifier,
) {
    val locale = LocalConfiguration.current.locales[0]
    val classNumber = absence.classesNumber?.let { stringResource(Strings.bars_attendance_class_number, it) }
    val time = remember(absence, locale, classNumber) {
        listOfNotNull(absence.date.format(locale), classNumber, absence.timeRange()).joinToString(" • ")
    }
    val kindAndTeacher = remember(absence) {
        listOfNotNull(
            absence.kind.raw.takeIf { it.isNotBlank() }?.capitalizeSafe(),
            absence.teacher,
        ).joinToString(" • ")
    }
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(MpeixTheme.palette.secondary, CardShape)
            .padding(vertical = 12.dp, horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp),
    ) {
        Text(
            text = time,
            style = MpeixTheme.typography.bodySmall,
            color = MpeixTheme.palette.onSurfaceVariant,
        )
        Text(
            text = absence.disciplineName,
            style = MpeixTheme.typography.bodyLarge,
            color = MpeixTheme.palette.onSurface,
        )
        if (kindAndTeacher.isNotEmpty()) {
            Text(
                text = kindAndTeacher,
                style = MpeixTheme.typography.bodyMedium,
                color = MpeixTheme.palette.onSurfaceVariant,
            )
        }
        ReasonBadge(
            isExcused = absence.isExcused,
            modifier = Modifier.padding(top = 4.dp),
        )
    }
}

/** Red like the unexcused part of the brief's bar, neutral for the excused ones. */
@Composable
private fun ReasonBadge(
    isExcused: Boolean,
    modifier: Modifier = Modifier,
) {
    val palette = MpeixTheme.palette
    val text = stringResource(if (isExcused) Strings.bars_attendance_excused else Strings.bars_attendance_not_excused)
    Text(
        text = text,
        style = MpeixTheme.typography.bodyMedium,
        color = if (isExcused) palette.onSurfaceVariant else palette.onMarkRed,
        modifier = modifier
            .background(
                color = if (isExcused) palette.surfaceContainerHighest else palette.markRed,
                shape = BadgeShape,
            )
            .padding(horizontal = 6.dp, vertical = 2.dp),
    )
}

@Composable
private fun CardsShimmer(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .shimmer(),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        repeat(SHIMMER_CARDS_COUNT) {
            Box(
                Modifier
                    .fillMaxWidth()
                    .height(64.dp)
                    .background(MpeixTheme.palette.secondary, CardShape)
            )
        }
    }
}

/** "6 апреля, пн" for ru and "6 April, Mon" for en. A date the calendar doesn't have is shown as on the site. */
private fun SiteDate.format(locale: Locale): String =
    runCatching { LocalDate.of(year, month, day).format(DateTimeFormatter.ofPattern("d MMMM, EEE", locale)) }
        .getOrElse { "${day.pad()}.${month.pad()}.$year" }

/** "15:35–17:10", or only the start when the end is unknown. */
private fun Absence.timeRange(): String? {
    val start = start ?: return null
    val end = end ?: return start.format()
    return "${start.format()}–${end.format()}"
}

private fun SiteTime.format(): String = "${hour.pad()}:${minute.pad()}"

private fun Int.pad(): String = toString().padStart(2, '0')

@PreviewLightDark
@Composable
private fun BarsAttendanceScreenPreview() {
    MpeixTheme {
        BarsAttendanceScreen(
            state = BarsAttendanceState(
                disciplines = Resource.Data(PreviewDisciplineAttendance),
                absences = Resource.Data(PreviewAbsences),
            ),
            onBackClick = {},
            onRefresh = {},
            onReloadDisciplines = {},
            onReloadAbsences = {},
        )
    }
}

@PreviewLightDark
@Composable
private fun BarsAttendanceScreenLoadingPreview() {
    MpeixTheme {
        BarsAttendanceScreen(
            state = BarsAttendanceState(),
            onBackClick = {},
            onRefresh = {},
            onReloadDisciplines = {},
            onReloadAbsences = {},
        )
    }
}

/** Summary loaded without absences, the absences page failed. */
@PreviewLightDark
@Composable
private fun BarsAttendanceScreenEmptyAndErrorPreview() {
    MpeixTheme {
        BarsAttendanceScreen(
            state = BarsAttendanceState(
                disciplines = Resource.Data(
                    PreviewDisciplineAttendance.map {
                        it.copy(attendance = it.attendance.copy(excused = 0, unexcused = 0))
                    }
                ),
                absences = Resource.Error(SdkErrorException(SdkError.NoConnection(PreviewDiagnosticId))),
            ),
            onBackClick = {},
            onRefresh = {},
            onReloadDisciplines = {},
            onReloadAbsences = {},
        )
    }
}

private val PreviewDiagnosticId = DiagnosticId("preview")

private const val PreviewTau = "Теория автоматического управления, часть 2"
private const val PreviewElectronics = "Электроника"
private const val PreviewEnglish = "Иностранный язык"
private val PreviewCredit = Assessment.Credit("зачёт (без оценки)")

private val PreviewDisciplineAttendance = listOf(
    previewDisciplineAttendance("1", PreviewTau, Assessment.Exam("экзамен"), 48, 1, 3),
    previewDisciplineAttendance("2", PreviewElectronics, Assessment.GradedCredit("зачёт с оценкой"), 32, 0, 4),
    previewDisciplineAttendance("3", PreviewEnglish, PreviewCredit, 32, 2, 0),
    previewDisciplineAttendance("4", "Физическая культура и спорт", PreviewCredit, 16, 0, 0),
)

private fun previewDisciplineAttendance(
    id: String,
    name: String,
    assessment: Assessment,
    lessons: Int,
    excused: Int,
    unexcused: Int,
) = DisciplineAttendance(
    id = DisciplineId(id),
    title = "$name (${assessment.raw})",
    name = name,
    assessment = assessment,
    attendance = Attendance(lessons = lessons, excused = excused, unexcused = unexcused),
)

private val PreviewLecture = LessonKind.Lecture("лекция")

private val PreviewAbsences = listOf(
    previewAbsence("1", SiteDate(2026, 4, 10), 1, PreviewLecture, "Васильев А.А.", PreviewTau, isExcused = false),
    previewAbsence(
        "2", SiteDate(2026, 4, 2), 3, LessonKind.Lab("лабораторная работа"), "Белова М.М., Фролов Д.Д.",
        PreviewElectronics, isExcused = false,
    ),
    previewAbsence("3", SiteDate(2026, 3, 18), 1, PreviewLecture, "Васильев А.А.", PreviewTau, isExcused = true),
    previewAbsence(
        "4", SiteDate(2026, 3, 17), 5, LessonKind.Unknown("учебно-тренировочное занятие"), null,
        PreviewEnglish, isExcused = true,
    ),
)

@Suppress("LongParameterList")
private fun previewAbsence(
    id: String,
    date: SiteDate,
    classesNumber: Int,
    kind: LessonKind,
    teacher: String?,
    disciplineName: String,
    isExcused: Boolean,
) = Absence(
    id = AbsenceId(id),
    title = disciplineName,
    date = date,
    classesNumber = classesNumber,
    start = SiteTime(9, 20),
    end = SiteTime(10, 55),
    kind = kind,
    teacher = teacher,
    disciplineId = DisciplineId(disciplineName),
    disciplineName = disciplineName,
    isExcused = isExcused,
)
