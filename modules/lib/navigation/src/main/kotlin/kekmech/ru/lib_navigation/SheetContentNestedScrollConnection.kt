package kekmech.ru.lib_navigation

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.unit.Velocity

/**
 * Stands between the scrollable content of a sheet and the `ModalBottomSheet`, so the content
 * scrolls the way it did in the View bottom sheets.
 *
 * `ModalBottomSheet` alone gives the sheet everything the content didn't consume:
 * - the list scrolled to the top in the middle of a gesture keeps going and pulls the sheet down,
 * - the velocity left after a list fling hits the top throws the sheet down, and while the sheet
 *   settles back it grabs the next touch instead of the list.
 *
 * With this connection the sheet is pulled down only by a gesture which started with the content
 * already at the top, and only such a gesture passes its fling velocity to the sheet (to dismiss it).
 */
internal class SheetContentNestedScrollConnection : NestedScrollConnection {

    // The content scrolled during the current gesture
    private var isContentScrolled = false

    // The current gesture pulls the sheet down
    private var isSheetDragged = false

    // The finger is up, so the next scroll by the user belongs to a new gesture
    private var isGestureFinished = false

    override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset {
        // A new gesture which interrupted the fling of the previous one, so its onPostFling never came
        if (source == NestedScrollSource.UserInput && isGestureFinished) reset()
        return Offset.Zero
    }

    override fun onPostScroll(consumed: Offset, available: Offset, source: NestedScrollSource): Offset {
        if (source != NestedScrollSource.UserInput) return Offset.Zero
        if (consumed.y != 0f) isContentScrolled = true
        return when {
            // Only pulling the sheet down matters, the sheet itself takes care of pulling it back up
            available.y <= 0f -> Offset.Zero
            // The content reached the top in the middle of the gesture: it stops there
            isContentScrolled && !isSheetDragged -> Offset(x = 0f, y = available.y)
            else -> {
                isSheetDragged = true
                Offset.Zero
            }
        }
    }

    override suspend fun onPreFling(available: Velocity): Velocity {
        isGestureFinished = true
        return Velocity.Zero
    }

    override suspend fun onPostFling(consumed: Velocity, available: Velocity): Velocity {
        val isSheetDragged = isSheetDragged
        reset()
        return if (isSheetDragged) Velocity.Zero else Velocity(x = 0f, y = available.y)
    }

    private fun reset() {
        isContentScrolled = false
        isSheetDragged = false
        isGestureFinished = false
    }
}
