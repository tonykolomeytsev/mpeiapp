package kekmech.ru.timetable

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import kekmech.ru.coreui.Resources
import kekmech.ru.timetable.model.TimetableFragmentModel
import kekmech.ru.timetable.view.DayFragment
import kekmech.ru.timetable.view.fragments.*

class WeekAdapter(
    fragmentManager: FragmentManager,
    private val model: TimetableFragmentModel,
    context: Context
) :
    FragmentPagerAdapter(fragmentManager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    private val daysOfWeekNames = Resources.getStringArray(context, R.array.days_of_week)

    override fun getCount() = 6

    override fun getItem(position: Int): Fragment {
        val dayOfWeek = position + 2 // +2 потому что Sunday это 1
        // val couples = { model.getDaySchedule(dayOfWeek, model.today.weekOfSemester) }
        return ((daysFragments[dayOfWeek - 1] ?: error("Can't create instance of DayFragment for day=$dayOfWeek"))
            .newInstance() as DayFragment)
            //.apply { this.model = this@WeekAdapter.model }
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return daysOfWeekNames[position + 1] // +1 cause of zero element is Sunday. We don't need to show Sunday in timetable
    }

    companion object {
        private val daysFragments = mapOf(
            1 to MondayFragment::class.java,
            2 to TuesdayFragment::class.java,
            3 to WednesdayFragment::class.java,
            4 to ThursdayFragment::class.java,
            5 to FridayFragment::class.java,
            6 to SaturdayFragment::class.java
        )
    }
}