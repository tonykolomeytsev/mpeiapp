package kekmech.ru.feature_bars_impl.presentation.screen.main_compose.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.fastForEach
import com.kekmech.feature_bars_grades_api.Grade
import kekmech.ru.ui_theme.theme.MpeixTheme
import kotlin.math.roundToInt

@Immutable
internal data class DisciplineItem(
    val title: String,
    val teacher: String,
    val assessmentType: String,
    val controlActivities: List<Pair<String, Grade?>>,
    val intermediate: Grade?,
    val final: Grade?,
)

@Composable
internal fun DisciplineItem(
    onClick: () -> Unit,
    item: DisciplineItem,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .clickable(onClick = onClick)
            .background(
                color = MpeixTheme.palette.surface,
                shape = RoundedCornerShape(12.dp),
            )
            .padding(top = 12.dp, bottom = 12.dp, start = 12.dp, end = 12.dp),
    ) {
        val personAndType =
            remember { item.assessmentType + " • " + item.teacher.replaceFirst(" ", " ") }
        Text(
            text = personAndType,
            color = MpeixTheme.palette.primary,
            style = MpeixTheme.typography.labelMini,
        )
        Spacer(Modifier.height(6.dp))
        Text(
            text = item.title,
            color = MpeixTheme.palette.content,
            style = MpeixTheme.typography.paragraphBig,
        )
        if (item.controlActivities.isNotEmpty()) {
            Spacer(Modifier.height(6.dp))
            Text(
                text = "Контрольные мероприятия",
                style = MpeixTheme.typography.labelNormal,
                color = MpeixTheme.palette.contentDisabled,
            )
            Spacer(Modifier.height(8.dp))
            Column(
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                item.controlActivities.fastForEach { (title, grade) ->
                    ControlActivityItem(
                        title = title,
                        grade = grade?.float,
                    )
                }
            }
        }
        if (item.intermediate != null || item.final != null) {
            Spacer(Modifier.height(12.dp))
            Text(
                text = "Итоговые оценки",
                style = MpeixTheme.typography.labelNormal,
                color = MpeixTheme.palette.contentDisabled,
            )
        }
        if (item.intermediate != null) {
            Spacer(Modifier.height(8.dp))
            ControlActivityItem(
                title = item.assessmentType,
                grade = item.intermediate.float,
            )
        }
        if (item.final != null) {
            Spacer(Modifier.height(8.dp))
            ControlActivityItem(
                title = "Итог",
                grade = item.final.float,
            )
        }
    }
}

@Composable
private fun ControlActivityItem(
    title: String,
    grade: Float?,
    modifier: Modifier = Modifier,
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .background(
                color = MpeixTheme.palette.surfacePlus3,
                shape = RoundedCornerShape(4.dp),
            )
            .heightIn(28.dp)
            .padding(vertical = 6.dp, horizontal = 8.dp),
    ) {
        Text(
            text = title,
            style = MpeixTheme.typography.paragraphNormal,
            color = MpeixTheme.palette.content,
            modifier = Modifier.weight(1f),
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
        )
        if (grade != null) {
            MarkItem(
                string = grade.roundToInt().toString(),
                colors = grade.asMarkColor(),
            )
        }
    }
}

@Composable
private fun MarkItem(
    string: String,
    colors: Pair<Color, Color>,
) {
    Text(
        text = string,
        style = MpeixTheme.typography.labelBig,
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
private fun DisciplineItemPreview1() {
    MpeixTheme {
        DisciplineItem(
            onClick = { /* no-op */ },
            item = DisciplineItem(
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
            ),
        )
    }
}

@Preview
@Composable
private fun DisciplineItemPreview2() {
    MpeixTheme {
        DisciplineItem(
            onClick = { /* no-op */ },
            item = DisciplineItem(
                title = "Статистическая динамика автоматических систем",
                teacher = "Меркурьев И.В.",
                assessmentType = "Экзамен",
                controlActivities = listOf(),
                intermediate = null,
                final = null,
            )
        )
    }
}

@Preview
@Composable
private fun DisciplineItemPreview3() {
    MpeixTheme {
        DisciplineItem(
            onClick = { /* no-op */ },
            item = DisciplineItem(
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
                intermediate = Grade(4.0f),
                final = null,
            ),
        )
    }
}

@Preview
@Composable
private fun DisciplineItemPreview4() {
    MpeixTheme {
        DisciplineItem(
            onClick = { /* no-op */ },
            item = DisciplineItem(
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
        )
    }
}