package kekmech.ru.feed.presenter

import android.content.Context
import androidx.lifecycle.Observer
import kekmech.ru.core.Presenter
import kekmech.ru.core.Router
import kekmech.ru.core.Screens.*
import kekmech.ru.core.UpdateChecker
import kekmech.ru.core.scopes.ActivityScope
import kekmech.ru.coreui.adapter.BaseAdapter
import kekmech.ru.feed.IFeedFragment
import kekmech.ru.feed.items.*
import kekmech.ru.feed.model.FeedModel
import kotlinx.coroutines.*
import java.util.*
import javax.inject.Inject

@ActivityScope
class FeedPresenter @Inject constructor(
    private val model: FeedModel,
    private val context: Context,
    private val router: Router,
    private val updateChecker: UpdateChecker
) : Presenter<IFeedFragment>() {

    private var isNotifiedToRefresh: Boolean = false
    var view: IFeedFragment? = null
    var offset = 0
    val daysToLoadOnStart = 3
    val adapter by lazy { BaseAdapter.Builder()
        .registerViewTypeFactory(FeedDividerItem.Factory())
        .registerViewTypeFactory(CoupleItem.Factory())
        .registerViewTypeFactory(LunchItem.Factory())
        .registerViewTypeFactory(WeekendItem.Factory())
        .registerViewTypeFactory(WeekendStackItem.Factory())
        .build()
    }
    val menuAdapter = BaseAdapter.Builder()
        .registerViewTypeFactory(FeedMenuItem.Factory())
        .build()

    /**
     * subscribe to view events
     */
    override fun onResume(view: IFeedFragment) {
        model.isNeedToUpdate.observe(view, Observer {
            if (it == true) {
                model.nitifyFeedUpdated()
                refresh()
                model.groupNumber.observe(view, Observer {
                    view.setStatus(
                        "Группа $it",
                        model.formattedTodayStatus,
                        "Идет ${model.currentWeekNumber} учебная неделя"
                    )
                })
            }
        })
        this.view = view
        model.groupNumber.observe(view, Observer {
            view.setStatus(
                "Группа $it",
                model.formattedTodayStatus,
                "Идет ${model.currentWeekNumber} учебная неделя"
            )
        })
        GlobalScope.launch(Dispatchers.Main) {

            //delay(100)
            view.updateAdapterIfNull(adapter)
            view.onEditListener = { onStatusEdit() }
            view.bottomReachListener = { onScrollEnd() }

            delay(50)
            if (offset == 0) {
                onScrollEnd()
            } else if (isNotifiedToRefresh) {
                isNotifiedToRefresh = false
                refresh()
            }
            view.unlock()
            setupMenu()
        }
        GlobalScope.launch(Dispatchers.Main) {
            delay(1000)
            if (model.appLaunchCount % 3 == 0 && model.isNotShowedUpdateDialog) {
                updateChecker.check { url, desc ->
                    model.saveForceUpdateArgs(url, desc)
                    router.navigate(FEED_TO_FORCE)
                    model.isNotShowedUpdateDialog = false
                }
            }
        }
    }

    private fun setupMenu() {
        menuAdapter.baseItems.clear()
        menuAdapter.baseItems.add(
            FeedMenuItem("Добавить группу").apply {
                clickListener = {
                    GlobalScope.launch(Dispatchers.Main){
                        delay(100)
                        router.navigate(FEED_TO_ADD)
                    }
                }
            }
        )
        view?.updateMenu(menuAdapter)
    }

    private fun onStatusEdit() {
        //view?.showMenu()
        GlobalScope.launch(Dispatchers.Main){
            delay(100)
            router.navigate(FEED_TO_ADD)
        }
    }

    /**
     * unsubscribe to view events
     */
    override fun onPause(view: IFeedFragment) {
        this.view = null
    }

    private fun refresh() {
        offset = 1
        model.weekendOffset = 0
        clearAdapter()
        GlobalScope.launch(Dispatchers.Main) {
            loadOffsetCouples(0)
        }
    }

    private fun onScrollEnd() {
        val localOffset = offset
        offset++
        GlobalScope.launch(Dispatchers.Main) {
            loadOffsetCouples(localOffset + model.weekendOffset)
        }
    }

    private suspend fun loadOffsetCouples(localOffset: Int, refresh: Boolean = false) = withContext(Dispatchers.Main) {
        val couples = model.getDayCouples(localOffset, refresh = refresh)
        couples.forEach {
            adapter.baseItems.add(it)
            adapter.notifyItemChanged(adapter.baseItems.size - 1)
            delay(100) // TODO избавиться от задержки и придумать анимацию иначе
        }
        view?.unlock()
        if (offset < daysToLoadOnStart)
            onScrollEnd()
    }

    private fun clearAdapter() {
        adapter.baseItems.clear()
        adapter.notifyDataSetChanged()
    }

    fun notifyToRefresh() {
        isNotifiedToRefresh = true
    }

}