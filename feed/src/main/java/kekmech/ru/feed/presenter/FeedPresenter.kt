package kekmech.ru.feed.presenter

import android.os.Handler
import android.util.Log
import kekmech.ru.core.Presenter
import kekmech.ru.core.scopes.ActivityScope
import kekmech.ru.coreui.adapter.BaseAdapter
import kekmech.ru.feed.FeedFragment
import kekmech.ru.feed.items.CoupleItem
import kekmech.ru.feed.items.FeedDividerItem
import kekmech.ru.feed.items.LunchItem
import kekmech.ru.feed.model.FeedModel
import kotlinx.android.synthetic.main.fragment_feed.*
import kotlinx.coroutines.*
import javax.inject.Inject

@ActivityScope
class FeedPresenter @Inject constructor(
    private val model: FeedModel
) : Presenter<FeedFragment> {

    var view: FeedFragment? = null
    var offset = 0
    val adapter by lazy { BaseAdapter.Builder()
        .registerViewTypeFactory(FeedDividerItem.Factory())
        .registerViewTypeFactory(CoupleItem.Factory())
        .registerViewTypeFactory(LunchItem.Factory())
        .build()
    }

    /**
     * subscribe to view events
     */
    override fun onResume(view: FeedFragment) {
        this.view = view
        view.onScrollEndListener = { onScrollEnd() }

        if (view.recyclerView.adapter == null) {
            view.recyclerView.adapter = adapter
            adapter.notifyDataSetChanged()
        }

        Handler().postDelayed({
            if (offset == 0)
                onScrollEnd()
        }, 300)
    }

    /**
     * unsubscribe to view events
     */
    override fun onPause(view: FeedFragment) {
        view.onScrollEndListener = {}
        this.view = null
    }

    private fun onScrollEnd() {
        GlobalScope.launch(Dispatchers.Main) {
            view?.showLoading()
            val couples = model.getDayCouples(offset, refresh = false)
            clearAdapter()
            couples.forEach {
                adapter.baseItems.add(it)
                adapter.notifyItemChanged(adapter.baseItems.size - 1)
                delay(100) // TODO избавиться от задержки и придумать анимацию иначе
            }
//            adapter.notifyDataSetChanged()
            view?.hideLoading()
            offset++
        }

    }

    private fun clearAdapter() {
        val count = adapter.baseItems.size
        adapter.baseItems.clear()
        adapter.notifyItemRangeRemoved(0, count)
    }
}