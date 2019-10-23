package kekmech.ru.addscreen.presenter

import kekmech.ru.addscreen.IAddFragment
import kekmech.ru.core.Presenter
import kekmech.ru.core.Router
import kekmech.ru.core.Screens
import kekmech.ru.core.dto.CoupleNative
import kekmech.ru.core.dto.Schedule
import kekmech.ru.core.dto.Time
import kekmech.ru.core.usecases.SaveScheduleUseCase
import kekmech.ru.core.usecases.SetNeedToUpdateFeedUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class AddFragmentPresenter @Inject constructor(
    private val saveScheduleUseCase: SaveScheduleUseCase,
    private val setNeedToUpdateFeedUseCase: SetNeedToUpdateFeedUseCase,
    private val router: Router
) : Presenter<IAddFragment>() {
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
        view?.hideLoadButton()
        view?.disableEditText()
        view?.showLoading()
        htmlWorker = HTMLWorker()
        GlobalScope.launch(Dispatchers.IO) {
            try {
                val parserSchedule = htmlWorker?.tryGroupAsync(group)?.await()!!
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
                saveScheduleUseCase(schedule)
                withContext(Dispatchers.Main) {
                    setNeedToUpdateFeedUseCase(true)
                    router.popBackStack()
                }

            } catch (e: Exception) {
                // todo вывести сообщение об ошибке
                e.printStackTrace()
            }
            withContext(Dispatchers.Main) {
                view?.showLoadButton()
                view?.enableEditText()
                view?.hideLoading()
            }
        }
    }
}