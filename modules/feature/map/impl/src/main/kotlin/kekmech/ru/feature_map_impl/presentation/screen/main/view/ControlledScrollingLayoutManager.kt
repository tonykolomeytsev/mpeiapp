package kekmech.ru.feature_map_impl.presentation.screen.main.view

import android.content.Context
import androidx.recyclerview.widget.LinearLayoutManager

internal class ControlledScrollingLayoutManager(context: Context) : LinearLayoutManager(context) {

    var isScrollingEnabled = true

    override fun canScrollVertically(): Boolean {
        return super.canScrollVertically() and isScrollingEnabled
    }

    override fun canScrollHorizontally(): Boolean {
        return super.canScrollHorizontally() and isScrollingEnabled
    }
}

