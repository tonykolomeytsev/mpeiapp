package kekmech.ru.feature_bars_impl.presentation.screen.main.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.fastForEach
import io.github.tonykolomeytsev.mpeix.sdk.Discipline
import kekmech.ru.ext_kotlin.capitalizeSafe
import kekmech.ru.feature_bars_impl.presentation.compose.MarkItem
import kekmech.ru.feature_bars_impl.presentation.compose.PreviewDisciplines
import kekmech.ru.feature_bars_impl.presentation.compose.numeric
import kekmech.ru.ui_theme.theme.MpeixTheme
import kotlin.math.roundToInt
import kekmech.ru.res_strings.R.string as Strings

@Composable
internal fun DisciplineItem(
    discipline: Discipline,
    onClick: (Discipline) -> Unit,
    modifier: Modifier = Modifier,
) {
    val shape = RoundedCornerShape(12.dp)
    Column(
        modifier = modifier
            .clip(shape)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
            ) { onClick.invoke(discipline) }
            .fillMaxWidth()
            .background(
                color = MpeixTheme.palette.surfaceContainer,
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
        val personAndType = remember(discipline) {
            listOfNotNull(
                discipline.assessment.raw.capitalizeSafe(),
                discipline.teacher?.replaceFirst(" ", " ")
            )
                .joinToString(" • ")
        }
        val finalMark = discipline.finalGrade?.numeric
        val marks =
            remember(discipline) { discipline.controlPoints.mapNotNull { it.mark?.numeric } }
        Text(
            text = personAndType,
            color = MpeixTheme.palette.onSurfaceVariant,
            style = MpeixTheme.typography.bodyMedium,
            modifier = Modifier.padding(top = 4.dp, bottom = 6.dp),
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
        )
        if (finalMark != null) {
            MarkItem(
                text = "Итог: ${finalMark.roundToInt()}",
                mark = finalMark,
            )
        } else if (marks.isNotEmpty()) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                marks.fastForEach { mark ->
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
        Column(
            modifier = Modifier.padding(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            PreviewDisciplines.fastForEach { DisciplineItem(it, onClick = {}) }
        }
    }
}
