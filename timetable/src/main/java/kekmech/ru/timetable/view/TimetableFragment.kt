package kekmech.ru.timetable.view


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import kekmech.ru.timetable.R
import kekmech.ru.timetable.TimetableFragmentPresenter
import kotlinx.android.synthetic.main.fragment_timetable.*
import org.koin.android.ext.android.inject

class TimetableFragment : Fragment(), TimetableFragmentView {

    val presenter: TimetableFragmentPresenter by inject()

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
