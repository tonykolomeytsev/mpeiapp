package kekmech.ru.addscreen.presenter

import android.text.InputFilter
import android.text.Spanned
import kekmech.ru.addscreen.IAddFragment
import kekmech.ru.core.Presenter
import kekmech.ru.core.dto.CoupleNative
import kekmech.ru.core.dto.Schedule
import kekmech.ru.core.dto.Time
import kekmech.ru.core.usecases.SaveScheduleUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.regex.Pattern
import javax.inject.Inject

class AddFragmentPresenter @Inject constructor(
    private val saveScheduleUseCase: SaveScheduleUseCase
) : Presenter<IAddFragment> {
    private var view: IAddFragment? = null
    private var htmlWorker: HTMLWorker? = null

    /**
     * subscribe to view events
     */
    override fun onResume(view: IAddFragment) {
        this.view = view
        view.onSearchClickListener = this::onSearch
    }

    /**
     * unsubscribe to view events
     */
    override fun onPause(view: IAddFragment) {
        this.view = null
        htmlWorker = null
    }

    private fun onSearch(group: String) {
        htmlWorker = HTMLWorker(view?.web!!)
        GlobalScope.launch(Dispatchers.IO) {
            try {
                val parserSchedule = htmlWorker?.tryGroup(group)?.await()!!
                val universityWeek = Time(parserSchedule.firstCoupleDay)
                val schedule = Schedule(
                    0,
                    group,
                    universityWeek.weekOfYear,
                    universityWeek.weekOfSemester,
                    parserSchedule.couples.map {
                        CoupleNative(
                            0,
                            it.name,
                            it.teacher,
                            it.place,
                            it.timeStart,
                            it.timeEnd,
                            it.type,
                            it.num,
                            it.day,
                            it.week
                        )
                    },
                    "Schedule"
                )
                saveScheduleUseCase.save(schedule)

            } catch (e: Exception) {
                // todo вывести сообщение об ошибке
                e.printStackTrace()
            }
        }
    }

    class PartialInputFormatter(private val pattern: Regex): InputFilter {

        override fun filter(
            source: CharSequence?,
            start: Int,
            end: Int,
            dest: Spanned?,
            dstart: Int,
            dend: Int
        ): CharSequence {
            return ""
        }
    }


}