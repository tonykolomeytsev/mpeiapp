package kekmech.ru.timetable

import android.content.Context
import kekmech.ru.core.Presenter
import kekmech.ru.timetable.model.TimetableFragmentModel
import kekmech.ru.timetable.view.TimetableFragmentView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject
import kotlinx.coroutines.GlobalScope

class TimetableFragmentPresenter @Inject constructor(
    private val model: TimetableFragmentModel,
    private val context: Context
) : Presenter<TimetableFragmentView> {

    lateinit var weekAdapter: WeekAdapter

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

    }

    /**
     * unsubscribe to view events
     */
    override fun onPause(view: TimetableFragmentView) = Unit

}