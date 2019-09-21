package kekmech.ru.timetable.view


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.tabs.TabLayout
import dagger.android.support.DaggerFragment
import kekmech.ru.timetable.R
import kekmech.ru.timetable.WeekAdapter
import kotlinx.android.synthetic.main.fragment_timetable.*


class TimetableFragment : DaggerFragment(), TimetableFragmentView {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_timetable, container, false)
    }

    override fun onResume() {
        super.onResume()
        viewPager.adapter = WeekAdapter(childFragmentManager)
        tabLayout.setupWithViewPager(viewPager)
        tabLayout.tabMode = TabLayout.MODE_SCROLLABLE
    }

}
