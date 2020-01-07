package kekmech.ru.timetable

import android.content.Context
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import kekmech.ru.core.Presenter
import kekmech.ru.core.Router
import kekmech.ru.core.Screens
import kekmech.ru.core.Screens.TIMETABLE_TO_FORCE
import kekmech.ru.core.UpdateChecker
import kekmech.ru.core.dto.Time
import kekmech.ru.coreui.Resources
import kekmech.ru.coreui.adapter.BaseItem
import kekmech.ru.timetable.model.TimetableFragmentModel
import kekmech.ru.timetable.view.TimetableFragmentView
import kotlinx.coroutines.*
import java.util.*

class TimetableFragmentPresenter constructor(
    private val model: TimetableFragmentModel,
    private val context: Context,
    private val updateChecker: UpdateChecker,
    private val router: Router
) : Presenter<TimetableFragmentView>() {

    lateinit var weekAdapter: WeekAdapter
    private var isNecessaryDayOpened = false
    val today get() = model.today
    var lastWeekOffset: Int = -1
    var selectegPage
        get() = model.selectedPage
        set(value) { model.selectedPage = value }

    /**
     * subscribe to view events
     */
    override fun onResume(view: TimetableFragmentView) {
        Log.d("Timetable", "onResume")
        GlobalScope.launch(Dispatchers.IO) {
            weekAdapter = WeekAdapter(view.getChildFragmentManager(), model, context)
            withContext(Dispatchers.Main) { view.setupViewPager() }
            model.updateScheduleFromRemote()
        }
        model.groupNumber.observe(view, Observer {
            val title = "Группа $it"
            view.setTitle(title)
        })
        // Смотрим следующую неделю
        view.onChangeParityClickListener = { onChangeParity() }
        model.weekOffset.observe(view, Observer {
            if (lastWeekOffset == it) return@Observer
            lastWeekOffset = it
            Log.d("PARITY", "onChangeParity $it")
            if (model.currentWeekNumber in 1..16) {
                if (it == 1) {
                    view.setBottomButtonText(
                        Resources.getString(
                            context,
                            R.string.timetable_show_current_week
                        )
                    )
                    view.setSubtitleStatus(
                        "Следующая ${model.currentWeekNumber + 1} неделя (${getParity(
                            today.getDayWithOffset(7)
                        )})"
                    )
                } else {
                    view.setBottomButtonText(
                        Resources.getString(
                            context,
                            R.string.timetable_show_next_week
                        )
                    )
                    view.setSubtitleStatus(
                        "Идет ${model.currentWeekNumber} неделя (${getParity(
                            today
                        )})"
                    )
                }
            } else {
                if (it == 1) {
                    view.setBottomButtonText(Resources.getString(context, R.string.timetable_show_current_week))
                    view.setSubtitleStatus(
                        "${getParity(today.getDayWithOffset(7)).capitalize()} неделя")
                } else {
                    view.setBottomButtonText(Resources.getString(context, R.string.timetable_show_next_week))
                    view.setSubtitleStatus(
                        "${getParity(today).capitalize()} неделя")
                }
            }
        })
        // проверка обновлений
        GlobalScope.launch(Dispatchers.Main) {
            delay(1000)
            if (model.isNotShowedUpdateDialog) {
                updateChecker.check { url, desc ->
                    model.saveForceUpdateArgs(url, desc)
                    router.navigate(TIMETABLE_TO_FORCE)
                    model.isNotShowedUpdateDialog = false
                }
            }
        }
    }

    fun onChangeParity() {
        if (lastWeekOffset == 0) {
            (model.weekOffset as MutableLiveData).value = 1
        } else {
            (model.weekOffset as MutableLiveData).value = 0
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

}