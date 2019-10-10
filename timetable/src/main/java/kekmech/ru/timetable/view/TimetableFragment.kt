package kekmech.ru.timetable.view


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import com.google.android.material.tabs.TabLayout
import dagger.android.support.DaggerFragment
import kekmech.ru.coreui.Resources
import kekmech.ru.timetable.R
import kekmech.ru.timetable.TimetableFragmentPresenter
import kekmech.ru.timetable.WeekAdapter
import kotlinx.android.synthetic.main.fragment_timetable.*
import javax.inject.Inject


class TimetableFragment : DaggerFragment(), TimetableFragmentView {

    @Inject
    lateinit var presenter: TimetableFragmentPresenter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_timetable, container, false)
    }

    override fun onResume() {
        super.onResume()
        presenter.onResume(this)
        activity?.window?.statusBarColor = Resources.getColor(context, R.color.colorSecondary)
        if (presenter.checkIsNecessaryDayOpened()) {
            val necessaryDay = presenter.today.dayOfWeek - 2 // потому что Calendar.MONDAY == 2
            if (necessaryDay in 0..6) {
                viewPager?.postDelayed({
                    viewPager?.setCurrentItem(necessaryDay, true)
                }, 100)
            }
        }
    }

    override fun onPause() {
        presenter.onPause(this)
        super.onPause()
    }

    override fun setupViewPager() {
        viewPager.adapter = presenter.weekAdapter
        viewPager.offscreenPageLimit = 5
        tabLayout.setupWithViewPager(viewPager)
        tabLayout.tabMode = TabLayout.MODE_SCROLLABLE
    }

    override fun setStatus(title: String, subtitle: String) {
        timetableToolbar?.post {
            timetableToolbar?.title = title
            timetableToolbar?.subtitle = subtitle
        }
    }

}
