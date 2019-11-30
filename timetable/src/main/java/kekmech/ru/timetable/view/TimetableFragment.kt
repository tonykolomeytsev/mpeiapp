package kekmech.ru.timetable.view


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import dagger.android.support.DaggerFragment
import kekmech.ru.coreui.Resources
import kekmech.ru.timetable.R
import kekmech.ru.timetable.TimetableFragmentPresenter
import kekmech.ru.timetable.WeekAdapter
import kotlinx.android.synthetic.main.fragment_timetable.*
import java.util.*
import javax.inject.Inject


class TimetableFragment : DaggerFragment(), TimetableFragmentView {

    @Inject
    lateinit var presenter: TimetableFragmentPresenter

    override var onChangeParityClickListener: () -> Unit = {}

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
        if (presenter.checkIsNecessaryDayOpened()) {
            val necessaryDay = presenter.today.dayOfWeek - 2 // потому что Calendar.MONDAY == 2
            if (necessaryDay in 0 until 5) {
                viewPager?.post {
                    viewPager?.setCurrentItem(necessaryDay, true)
                }
                presenter.selectegPage = necessaryDay
            } else { // show next monday if today is saturday or sunday
                onChangeParityClickListener()
                viewPager?.post {
                    viewPager?.setCurrentItem(0, true)
                    presenter.lastWeekOffset = 1
                }
                presenter.selectegPage = 0
            }
        } else {
            viewPager?.post {
                viewPager?.setCurrentItem(presenter.selectegPage, true)
            }
        }
        viewPager?.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) = Unit
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) = Unit
            override fun onPageSelected(position: Int) {
                presenter.selectegPage = (position)
            }
        })
        buttonChangeWeekParity?.setOnClickListener { onChangeParityClickListener() }
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
        timetableToolbar?.title = title
        timetableToolbar?.subtitle = subtitle
    }

    override fun setSubtitleStatus(subtitle: String) {
        timetableToolbar?.subtitle = subtitle
    }

    override fun setBottomButtonText(string: String) {
        textViewChangeWeekParity.text = string
    }

    override fun setTitle(title: String) {
        timetableToolbar?.title = title
    }

}
