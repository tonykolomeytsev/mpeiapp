package kekmech.ru.feature_bars_impl.presentation.screen.main_compose.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.fastFirstOrNull
import androidx.compose.ui.util.fastForEach
import androidx.compose.ui.util.fastMap
import kekmech.ru.feature_bars_impl.domain.AssessedDiscipline
import kekmech.ru.feature_bars_impl.domain.ControlActivity
import kekmech.ru.feature_bars_impl.domain.FinalGrade
import kekmech.ru.feature_bars_impl.domain.FinalGradeType
import kekmech.ru.feature_bars_impl.presentation.compose.MarkItem
import kekmech.ru.ui_theme.theme.MpeixTheme
import kotlin.math.roundToInt
import kekmech.ru.res_strings.R.string as Strings

@Immutable
internal data class DisciplineUiItem(
    val name: String,
    val person: String,
    val type: String,
    val finalMark: Float?,
    val marks: List<Float>,
    val discipline: AssessedDiscipline,
)

@Composable
internal fun DisciplineItem(
    discipline: DisciplineUiItem,
    onClick: (DisciplineUiItem) -> Unit,
    modifier: Modifier = Modifier,
) {
    val shape = RoundedCornerShape(8.dp)
    Column(
        modifier = modifier
            .clip(shape)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
            ) { onClick.invoke(discipline) }
            .fillMaxWidth()
            .background(
                color = MpeixTheme.palette.surface,
                shape = shape,
            )
            .padding(vertical = 12.dp, horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp),
    ) {
        Text(
            text = discipline.name,
            color = MpeixTheme.palette.onSurface,
            style = MpeixTheme.typography.bodyLarge,
        )
        val personAndType = remember { discipline.type + " • " + discipline.person.replaceFirst(" ", " ") }
        Text(
            text = personAndType,
            color = MpeixTheme.palette.onSurfaceVariant,
            style = MpeixTheme.typography.bodyMedium,
            modifier = Modifier.padding(top = 4.dp, bottom = 6.dp),
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
        )
        if (discipline.finalMark != null) {
            MarkItem(
                text = "Итог: ${discipline.finalMark.roundToInt()}",
                mark = discipline.finalMark,
            )
        } else if (discipline.marks.isNotEmpty()) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                discipline.marks.fastForEach { mark ->
                    MarkItem(
                        text = mark.roundToInt().toString(),
                        mark = mark,
                    )
                }
            }
        } else {
            NoMarksItem()
        }
    }
}

@Composable
private fun NoMarksItem() {
    Text(
        text = stringResource(Strings.bars_item_no_marks),
        style = MpeixTheme.typography.bodyLarge,
        color = MpeixTheme.palette.onSurfaceVariant,
        modifier = Modifier
            .background(
                color = MpeixTheme.palette.surfaceContainerHighest,
                shape = RoundedCornerShape(4.dp),
            )
            .padding(horizontal = 6.dp, vertical = 2.dp)
    )
}

@Preview
@Composable
private fun DisciplineItemPreview() {
    MpeixTheme {
        Scaffold(
            containerColor = MpeixTheme.palette.surface,
        ) { innerPadding ->
            Column(
                modifier = Modifier
                    .padding(innerPadding)
                    .padding(horizontal = 8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                DisciplineItem(
                    AssessedDiscipline(
                        name = "Иностранный язык",
                        person = "Чеботарева О.А.",
                        assessmentType = "Зачёт с оценкой",
                        controlActivities = emptyList(),
                        finalGrades = listOf(
                            FinalGrade(
                                name = "Зачет",
                                finalMark = 4.0f,
                                type = FinalGradeType.FINAL_MARK,
                            )
                        ),
                    ).toUi(),
                    {}
                )
                DisciplineItem(
                    AssessedDiscipline(
                        name = "Организационное поведение",
                        person = "Кирилина Т.Ю.",
                        assessmentType = "Зачёт (без оценки) (по совокупности)",
                        controlActivities = listOf(
                            ControlActivity(
                                name = "",
                                weight = "",
                                deadline = "",
                                finalMark = 1.0f,
                            ),
                            ControlActivity(
                                name = "",
                                weight = "",
                                deadline = "",
                                finalMark = 2.0f,
                            ),
                            ControlActivity(
                                name = "",
                                weight = "",
                                deadline = "",
                                finalMark = 3.0f,
                            ),
                            ControlActivity(
                                name = "",
                                weight = "",
                                deadline = "",
                                finalMark = 4.0f,
                            ),
                            ControlActivity(
                                name = "",
                                weight = "",
                                deadline = "",
                                finalMark = 5.0f,
                            ),
                        ),
                        finalGrades = emptyList(),
                    ).toUi(),
                    {}
                )
                DisciplineItem(
                    AssessedDiscipline(
                        name = "Проектный менеджмент",
                        person = "Сотниченко Е.",
                        assessmentType = "Зачёт (без оценки) (по совокупности)",
                        controlActivities = emptyList(),
                        finalGrades = emptyList(),
                    ).toUi(),
                    {}
                )
                DisciplineItem(
                    AssessedDiscipline(
                        name = "Промышленная робототехника",
                        person = "Орлов И.В.",
                        assessmentType = "Зачёт с оценкой",
                        controlActivities = emptyList(),
                        finalGrades = emptyList(),
                    ).toUi(),
                    {}
                )
                DisciplineItem(
                    AssessedDiscipline(
                        name = "Статистическая динамика автоматических систем",
                        person = "Меркурьев И.В",
                        assessmentType = "Экзамен",
                        controlActivities = emptyList(),
                        finalGrades = emptyList(),
                    ).toUi(),
                    {}
                )
                DisciplineItem(
                    AssessedDiscipline(
                        name = "Теория принятия решений",
                        person = "Еремеев А.П.",
                        assessmentType = "Зачёт (без оценки) (по совокупности)",
                        controlActivities = emptyList(),
                        finalGrades = emptyList(),
                    ).toUi(),
                    {}
                )
                DisciplineItem(
                    AssessedDiscipline(
                        name = "Управление движением мобильных колесных роботов",
                        person = "Адамов Б.И.",
                        assessmentType = "Экзамен",
                        controlActivities = emptyList(),
                        finalGrades = emptyList(),
                    ).toUi(),
                    {}
                )
                DisciplineItem(
                    AssessedDiscipline(
                        name = "Учебная практика: научно-исследовательская работа",
                        person = "Комерзан Е.В.",
                        assessmentType = "Зачёт с оценкой",
                        controlActivities = emptyList(),
                        finalGrades = emptyList(),
                    ).toUi(),
                    {}
                )
            }
        }

    }
}

private fun AssessedDiscipline.toUi() =
    DisciplineUiItem(
        name = name,
        person = person,
        type = assessmentType,
        finalMark = finalGrades.fastFirstOrNull { it.type == FinalGradeType.FINAL_MARK }?.finalMark,
        marks = controlActivities.fastMap { it.finalMark },
        discipline = this,
    )