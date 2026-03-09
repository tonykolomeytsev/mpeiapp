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
import androidx.compose.ui.graphics.Color
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
            color = MpeixTheme.palette.content,
            style = MpeixTheme.typography.paragraphBig,
        )
        val personAndType = remember { discipline.type + " • " + discipline.person.replaceFirst(" ", " ") }
        Text(
            text = personAndType,
            color = MpeixTheme.palette.contentDisabled,
            style = MpeixTheme.typography.paragraphNormal,
            modifier = Modifier.padding(top = 4.dp, bottom = 6.dp),
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
        )
        if (discipline.finalMark != null) {
            MarkItem(
                string = "Итог: ${discipline.finalMark.roundToInt()}",
                colors = discipline.finalMark.asMarkColor(),
            )
        } else if (discipline.marks.isNotEmpty()) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                discipline.marks.fastForEach { mark ->
                    MarkItem(
                        string = mark.roundToInt().toString(),
                        colors = mark.asMarkColor(),
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
        style = MpeixTheme.typography.paragraphNormalAccent,
        color = MpeixTheme.palette.contentDisabled,
        modifier = Modifier
            .background(
                color = MpeixTheme.palette.surfacePlus3,
                shape = RoundedCornerShape(4.dp),
            )
            .padding(horizontal = 6.dp, vertical = 2.dp)
    )
}

@Composable
private fun MarkItem(
    string: String,
    colors: Pair<Color, Color>,
) {
    Text(
        text = string,
        style = MpeixTheme.typography.paragraphNormalAccent,
        color = colors.first,
        modifier = Modifier
            .background(
                color = colors.second,
                shape = RoundedCornerShape(4.dp),
            )
            .padding(horizontal = 6.dp, vertical = 2.dp)
    )
}

@Composable
private fun Float.asMarkColor(): Pair<Color, Color> =
    with(MpeixTheme.palette) {
        when (this@asMarkColor) {
            in 3.5f..5f -> greenMarkColor to greenBgMarkColor
            in 2.5f..<3.5f -> yellowMarkColor to yellowBgMarkColor
            in 0f..<2.5f -> redMarkColor to redBgMarkColor
            else -> contentDisabled to surfacePlus3
        }
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