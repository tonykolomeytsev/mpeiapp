package kekmech.ru.coreui

import android.animation.AnimatorInflater
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.appbar.AppBarLayout

public fun RecyclerView.attachScrollListenerForAppBarLayoutShadow(appBarLayout: AppBarLayout) {
    appBarLayout.stateListAnimator = AnimatorInflater.loadStateListAnimator(context, R.animator.app_bar_elevation)
    appBarLayout.isLiftOnScroll = true
    addOnScrollListener(object : RecyclerView.OnScrollListener() {
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            appBarLayout.isSelected = recyclerView.canScrollVertically(-1)
        }
    })
}
