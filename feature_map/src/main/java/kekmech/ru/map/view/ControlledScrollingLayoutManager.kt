package kekmech.ru.map.view

import android.content.Context
import androidx.recyclerview.widget.LinearLayoutManager

class ControlledScrollingLayoutManager(context: Context) : LinearLayoutManager(context) {

    var isScrollingEnabled = true

    override fun canScrollVertically(): Boolean {
        return super.canScrollVertically() and isScrollingEnabled
    }

    override fun canScrollHorizontally(): Boolean {
        return super.canScrollHorizontally() and isScrollingEnabled
    }
}

