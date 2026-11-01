package kekmech.ru.ui_kit_notes

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kekmech.ru.ui_shimmer.shimmer
import kekmech.ru.ui_theme.theme.MpeixTheme
import java.time.LocalDate

/**
 * # NoteItem
 *
 * Sticky note card with the note [content], relative [date] and [disciplineName] of the classes.
 * E-mails, links and phone numbers in the [content] are clickable.
 *
 * Usage example:
 * ```kotlin
 * NoteItem(
 *     content = note.content,
 *     date = note.dateTime.toLocalDate(),
 *     disciplineName = note.classesName,
 *     onClick = { onNoteClick(note) },
 * )
 * ```
 *
 * @param content the text of the note
 * @param date the date of the classes the note is attached to
 * @param disciplineName the name of the classes the note is attached to
 * @param modifier [Modifier] to be applied to the note
 * @param onClick called when the note is clicked, or null if the note is not clickable
 */
@Composable
public fun NoteItem(
    content: String,
    date: LocalDate,
    disciplineName: String,
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null,
) {
    val linkifiedContent = remember(content) { linkify(content) }
    Column(
        modifier = modifier
            .fillMaxWidth()
            .noteBackground(
                color = MpeixTheme.palette.secondary,
                foldColor = MpeixTheme.palette.outlineVariant,
            )
            .then(if (onClick != null) Modifier.clickable(onClick = onClick) else Modifier)
            .padding(start = 16.dp, end = 24.dp, top = 12.dp, bottom = 16.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp),
    ) {
        Text(
            text = linkifiedContent,
            style = MpeixTheme.typography.bodyLarge,
            color = MpeixTheme.palette.onSurface,
        )
        Text(
            text = relativeDateString(date),
            style = MpeixTheme.typography.labelMedium,
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium,
            color = MpeixTheme.palette.primary,
        )
        Text(
            text = disciplineName,
            style = MpeixTheme.typography.labelMedium,
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium,
            color = MpeixTheme.palette.onSurfaceVariant,
        )
    }
}

/**
 * Placeholder of the [NoteItem] while notes are loading.
 */
@Composable
public fun NoteItemShimmer(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .shimmer()
            .fillMaxWidth()
            .height(88.dp)
            .background(
                color = MpeixTheme.palette.outlineVariant,
                shape = RoundedCornerShape(8.dp),
            ),
    )
}

@PreviewLightDark
@Composable
private fun NoteItemPreview() {
    MpeixTheme {
        Column(
            modifier = Modifier
                .background(MpeixTheme.palette.surface)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            NoteItem(
                content = "Сдать типовой расчет №2, подробности на mpei.ru",
                date = LocalDate.now().plusDays(1),
                disciplineName = "Технология металлов",
                onClick = {},
            )
            NoteItem(
                content = "Написать на почту преподавателю ivanov@mpei.ru",
                date = LocalDate.now().plusDays(7),
                disciplineName = "Теория автоматического управления",
            )
            NoteItemShimmer()
        }
    }
}
