package kekmech.ru.ui_kit_lists

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.drawscope.translate
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kekmech.ru.ui_theme.theme.MpeixTheme
import kekmech.ru.res_icons.R.drawable as IconsDrawable

private const val SwipeDismissThreshold = 0.5f

// colorRed of the old XML theme, the same in light and dark themes
private val SwipeDeleteBackground = Color(0xFFDB4437)

/**
 * # SwipeToDeleteItem
 *
 * Deletes [content] by swipe from end to start, like the old RecyclerView `SwipeToDeleteCallback`:
 * red rectangle appears only in the area left by [content], with delete icon at the end.
 *
 * The item is expected to be removed from the list right after [onDelete] call,
 * so it never settles back.
 *
 * Usage example:
 * ```kotlin
 * items(items, key = { it.id }) { item ->
 *     SwipeToDeleteItem(onDelete = { onDelete(item) }) {
 *         ListItem(headlineText = item.name)
 *     }
 * }
 * ```
 *
 * @param onDelete called when the item was swiped by half of its width
 * @param modifier [Modifier] to be applied to the item
 * @param content the item content, it should have opaque background
 */
@Composable
public fun SwipeToDeleteItem(
    onDelete: () -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    val currentOnDelete by rememberUpdatedState(onDelete)
    val state = rememberSwipeToDismissBoxState(
        confirmValueChange = { value ->
            if (value == SwipeToDismissBoxValue.EndToStart) currentOnDelete()
            // Item is removed from the list by the store, so it never has to settle back
            value == SwipeToDismissBoxValue.EndToStart
        },
        // ItemTouchHelper dismisses an item swiped by half of its width
        positionalThreshold = { totalDistance -> totalDistance * SwipeDismissThreshold },
    )
    SwipeToDismissBox(
        state = state,
        modifier = modifier,
        enableDismissFromStartToEnd = false,
        backgroundContent = {
            val iconPainter = painterResource(IconsDrawable.ic_delete_black_24)
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .drawBehind {
                        // Offset is negative while swiping from end to start
                        val offset = state.requireOffset().coerceAtMost(0f)
                        drawRect(
                            color = SwipeDeleteBackground,
                            topLeft = Offset(size.width + offset, 0f),
                            size = Size(-offset, size.height),
                        )
                        // Icon is drawn under the content, so it shows up only in the revealed area
                        val iconSize = iconPainter.intrinsicSize
                        val iconMargin = (size.height - iconSize.height) / 2f
                        translate(
                            left = size.width - iconMargin - iconSize.width,
                            top = iconMargin,
                        ) {
                            with(iconPainter) {
                                draw(iconSize, colorFilter = ColorFilter.tint(Color.White))
                            }
                        }
                    },
            )
        },
    ) {
        content()
    }
}

@Preview
@Composable
private fun SwipeToDeleteItemPreview() {
    MpeixTheme {
        SwipeToDeleteItem(onDelete = {}) {
            Text(
                text = "Swipe me to the left",
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MpeixTheme.palette.surface)
                    .padding(16.dp),
                style = MpeixTheme.typography.bodyLarge,
                color = MpeixTheme.palette.onSurface,
            )
        }
    }
}
