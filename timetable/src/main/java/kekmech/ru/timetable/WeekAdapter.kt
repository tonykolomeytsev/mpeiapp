package kekmech.ru.timetable

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import kekmech.ru.core.dto.CoupleNative
import kekmech.ru.coreui.Resources
import kekmech.ru.timetable.model.TimetableFragmentModel
import kekmech.ru.timetable.view.DayFragment

class WeekAdapter(
    fragmentManager: FragmentManager,
    private val model: TimetableFragmentModel,
    context: Context
) :
    FragmentPagerAdapter(fragmentManager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    private val daysOfWeekNames = Resources.getStringArray(context, R.array.days_of_week)

    override fun getCount() = 6

    override fun getItem(position: Int): Fragment { // +2 потому что Sunday это 1
        val couples = { model.getDaySchedule(position + 2, model.today.weekOfSemester) }
        return DayFragment(couples)
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return daysOfWeekNames[position + 1] // +1 cause of zero element is Sunday. We don't need to show Sunday in timetable
    }
}