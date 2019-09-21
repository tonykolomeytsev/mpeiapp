package kekmech.ru.timetable

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import kekmech.ru.core.dto.CoupleNative
import kekmech.ru.timetable.model.TimetableFragmentModel
import kekmech.ru.timetable.view.DayFragment

class WeekAdapter(
    fragmentManager: FragmentManager/*,
    private val model: TimetableFragmentModel*/
) :
    FragmentPagerAdapter(fragmentManager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    override fun getCount() = 6

    override fun getItem(position: Int): Fragment {
        return DayFragment(monday)
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return "Понедельник"
    }

    val monday by lazy { listOf(
        CoupleNative(1,
            "Защита интеллектуальной собственности и патентоведение",
            "Комерзан Е.В.",
            "C-213",
            "9:20",
            "10:50",
            CoupleNative.LECTURE,
            1,1, CoupleNative.ODD),
        CoupleNative(1,
            "Вычислительная механика",
            "Адамов Б.И.",
            "C-213",
            "11:10",
            "12:45",
            CoupleNative.LECTURE,
            2,1, CoupleNative.BOTH),
        CoupleNative(1,
            "Вычислительная механика",
            "Адамов Б.И.",
            "C-213",
            "13:45",
            "15:20",
            CoupleNative.LAB,
            3,1, CoupleNative.BOTH),
        CoupleNative(1,
            "Гидропневмопривод мехатронных и робототехнических систем",
            "Зуев Ю.Ю.",
            "C-213",
            "15:35",
            "17:10",
            CoupleNative.PRACTICE,
            4,1, CoupleNative.BOTH)
    ) }
}