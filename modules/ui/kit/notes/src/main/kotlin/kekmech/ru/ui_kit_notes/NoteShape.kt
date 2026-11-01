package kekmech.ru.ui_kit_notes

import androidx.compose.foundation.background
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp

private val CornerRadius = 8.dp
private val FoldSize = 24.dp

/**
 * Shape of the sticky note: rounded rectangle with the top end corner cut off diagonally.
 * The cut corner is covered by the fold, see [noteBackground].
 */
public object NoteShape : Shape {

    override fun createOutline(size: Size, layoutDirection: LayoutDirection, density: Density): Outline {
        val radius = with(density) { CornerRadius.toPx() }
        val fold = with(density) { FoldSize.toPx() }
        val path = Path().apply {
            moveTo(0f, radius)
            arcTo(Rect(0f, 0f, 2 * radius, 2 * radius), 180f, 90f, false)
            lineTo(size.width - fold, 0f)
            lineTo(size.width, fold)
            lineTo(size.width, size.height - radius)
            arcTo(Rect(size.width - 2 * radius, size.height - 2 * radius, size.width, size.height), 0f, 90f, false)
            lineTo(radius, size.height)
            arcTo(Rect(0f, size.height - 2 * radius, 2 * radius, size.height), 90f, 90f, false)
            close()
        }
        return Outline.Generic(path)
    }
}

/**
 * Background of the sticky note: [NoteShape] filled with [color],
 * with the folded corner of [foldColor] at the top end.
 */
public fun Modifier.noteBackground(color: Color, foldColor: Color): Modifier =
    this
        .clip(NoteShape)
        .background(color)
        .drawWithContent {
            drawContent()
            val radius = CornerRadius.toPx()
            val fold = FoldSize.toPx()
            val start = size.width - fold
            // Lower left half of the cut corner square with rounded bottom left corner
            val path = Path().apply {
                moveTo(start, 0f)
                lineTo(size.width, fold)
                lineTo(start + radius, fold)
                arcTo(Rect(start, fold - 2 * radius, start + 2 * radius, fold), 90f, 90f, false)
                close()
            }
            drawPath(path, foldColor)
        }
