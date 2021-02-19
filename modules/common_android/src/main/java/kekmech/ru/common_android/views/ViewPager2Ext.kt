package kekmech.ru.common_android.views

import androidx.viewpager2.widget.ViewPager2

fun ViewPager2.onPageSelected(callback: (position: Int) -> Unit) =
    registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
        override fun onPageSelected(position: Int) = callback(position)
    })