package kekmech.ru.timetable.view

import androidx.fragment.app.FragmentManager
import androidx.lifecycle.LifecycleOwner

interface TimetableFragmentView : LifecycleOwner {
    fun getChildFragmentManager(): FragmentManager
    fun setupViewPager()
    fun setStatus(title: String, subtitle: String)
}