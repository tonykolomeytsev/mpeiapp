package kekmech.ru.ext_android.views

import androidx.annotation.Px
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout

/**
 * The refresh spinner rests with its top edge [offset] px below the top edge of the view
 * (the framework counts the offset of the spinner, not of its center).
 */
public fun SwipeRefreshLayout.setProgressViewOffset(@Px offset: Int) {
    setProgressViewOffset(false, 0, offset)
}

/** Space the spinner occupies when it rests: content moved down by it is not covered by the spinner. */
@get:Px
public val SwipeRefreshLayout.restingSpinnerExtent: Int
    get() = progressCircleDiameter
