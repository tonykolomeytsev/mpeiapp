package kekmech.ru.timetable

import kekmech.ru.core.Presenter
import kekmech.ru.timetable.model.TimetableFragmentModel
import kekmech.ru.timetable.view.TimetableFragmentView
import javax.inject.Inject

class TimetableFragmentPresenter @Inject constructor(
    private val model: TimetableFragmentModel
) : Presenter<TimetableFragmentView> {
    /**
     * subscribe to view events
     */
    override fun onResume(view: TimetableFragmentView) = Unit

    /**
     * unsubscribe to view events
     */
    override fun onPause(view: TimetableFragmentView) = Unit

}