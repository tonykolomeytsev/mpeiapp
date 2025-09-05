package kekmech.ru.common_android.views

import androidx.annotation.Px
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout

public fun SwipeRefreshLayout.setProgressViewOffset(@Px offset: Int) {
    setProgressViewOffset(false, 0, offset + progressCircleDiameter / 2)
}
