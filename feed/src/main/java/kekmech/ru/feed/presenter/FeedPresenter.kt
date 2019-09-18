package kekmech.ru.feed.presenter

import android.content.Context
import android.os.Handler
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.Observer
import kekmech.ru.core.Presenter
import kekmech.ru.core.Router
import kekmech.ru.core.Screens
import kekmech.ru.core.dto.Time
import kekmech.ru.core.scopes.ActivityScope
import kekmech.ru.coreui.adapter.BaseAdapter
import kekmech.ru.feed.Dialogs
import kekmech.ru.feed.IFeedFragment
import kekmech.ru.feed.R
import kekmech.ru.feed.items.CoupleItem
import kekmech.ru.feed.items.FeedDividerItem
import kekmech.ru.feed.items.LunchItem
import kekmech.ru.feed.items.WeekendItem
import kekmech.ru.feed.model.FeedModel
import kotlinx.coroutines.*
import java.util.*
import javax.inject.Inject

@ActivityScope
class FeedPresenter @Inject constructor(
    private val model: FeedModel,
    private val context: Context,
    private val router: Router
) : Presenter<IFeedFragment> {

    var view: IFeedFragment? = null
    var offset = 0
    val daysToLoadOnStart = 3
    val adapter by lazy { BaseAdapter.Builder()
        .registerViewTypeFactory(FeedDividerItem.Factory())
        .registerViewTypeFactory(CoupleItem.Factory())
        .registerViewTypeFactory(LunchItem.Factory())
        .registerViewTypeFactory(WeekendItem.Factory())
        .build()
    }

    /**
     * subscribe to view events
     */
    override fun onResume(view: IFeedFragment) {
        this.view = view
        GlobalScope.launch(Dispatchers.Main) {

            val group: String = withContext(Dispatchers.IO) { model.groupNumber }.toUpperCase()
            view.setStatus(
                "Группа $group",
                model.formattedTodayStatus,
                "Идет ${model.currentWeekNumber} учебная неделя"
            )

            delay(100)
            view.updateAdapterIfNull(adapter)
            view.onEditListener = { onStatusEdit() }
            view.bottomReachListener = { onScrollEnd() }

            delay(50)
            if (offset == 0)
                onScrollEnd()
            view.unlock()
        }
    }

    private fun onStatusEdit() {
        view?.withinContext {
            val dialog = Dialogs.listDialog(it,"Редактировать", listOf("Кэшировать заново", "Сменить расписание")) { _, i ->
                Toast.makeText(context, i.toString(), Toast.LENGTH_SHORT).show()
            }
            GlobalScope.launch(Dispatchers.Main) {
                delay(50)
                //view?.showEditDialog(dialog)
            }
        }
        router.navigateTo(Screens.ADD)
    }

    /**
     * unsubscribe to view events
     */
    override fun onPause(view: IFeedFragment) {
        this.view = null
    }

    private fun onScrollEnd() {
        val localOffset = offset
        offset++
        GlobalScope.launch(Dispatchers.Main) {
            val couples = model.getDayCouples(localOffset, refresh = false)
            couples.forEach {
                adapter.baseItems.add(it)
                adapter.notifyItemChanged(adapter.baseItems.size - 1)
                delay(100) // TODO избавиться от задержки и придумать анимацию иначе
            }
            view?.unlock()
            if (offset < daysToLoadOnStart)
                onScrollEnd()
        }
    }

    private fun clearAdapter() {
        val count = adapter.baseItems.size
        adapter.baseItems.clear()
        adapter.notifyItemRangeRemoved(0, count)
    }

}