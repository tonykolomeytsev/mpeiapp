package kekmech.ru.common_analytics

import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2

fun RecyclerView.addScrollAnalytics(analytics: Analytics, elementName: String) {
    addOnScrollListener(object : RecyclerView.OnScrollListener() {
        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            if (newState == RecyclerView.SCROLL_STATE_DRAGGING) analytics.sendScroll(elementName)
        }
    })
}

fun ViewPager2.addScrollAnalytics(analytics: Analytics, elementName: String) {
    registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
        override fun onPageScrollStateChanged(state: Int) {
            if (state == ViewPager2.SCROLL_STATE_DRAGGING) analytics.sendScroll(elementName)
        }
    })
}