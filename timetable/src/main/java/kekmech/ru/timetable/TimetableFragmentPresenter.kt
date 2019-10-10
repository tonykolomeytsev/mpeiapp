package kekmech.ru.timetable

import android.content.Context
import kekmech.ru.core.Presenter
import kekmech.ru.core.dto.Time
import kekmech.ru.coreui.adapter.BaseItem
import kekmech.ru.timetable.model.TimetableFragmentModel
import kekmech.ru.timetable.view.TimetableFragmentView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject
import kotlinx.coroutines.GlobalScope
import java.util.*

class TimetableFragmentPresenter @Inject constructor(
    private val model: TimetableFragmentModel,
    private val context: Context
) : Presenter<TimetableFragmentView>() {

    lateinit var weekAdapter: WeekAdapter
    private var isNecessaryDayOpened = false
    val today get() = model.today

    /**
     * subscribe to view events
     */
    override fun onResume(view: TimetableFragmentView) {
        if (!this::weekAdapter.isInitialized) {
            GlobalScope.launch(Dispatchers.IO) {
                weekAdapter = WeekAdapter(view.getChildFragmentManager(), model, context)
                withContext(Dispatchers.Main) { view.setupViewPager() }
            }
        }
        GlobalScope.launch(Dispatchers.IO) {
            val title = "Группа ${model.groupNumber}"
            val subtitle = "Идет ${model.currentWeekNumber} неделя (${getParity(today)})"
            withContext(Dispatchers.Main) { view.setStatus(title, subtitle) }
        }
    }

    private fun getParity(today: Time) = if (today.parity == Time.Parity.EVEN) "четная" else "нечетная"

    /**
     * unsubscribe to view events
     */
    override fun onPause(view: TimetableFragmentView) = Unit

    fun checkIsNecessaryDayOpened(): Boolean {
        if (isNecessaryDayOpened) return false
        else {
            isNecessaryDayOpened = true
            return true
        }
    }

    companion object {
        val locker = Object()
        @Volatile var dayCouplesMap = mutableMapOf<Int, () -> (List<BaseItem<*>>)>()
    }

}