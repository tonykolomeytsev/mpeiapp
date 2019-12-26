package kekmech.ru.feed.presenter

import android.content.Context
import androidx.lifecycle.Observer
import kekmech.ru.core.Presenter
import kekmech.ru.core.Router
import kekmech.ru.core.Screens.*
import kekmech.ru.core.UpdateChecker
import kekmech.ru.coreui.adapter.BaseAdapter
import kekmech.ru.feed.IFeedFragment
import kekmech.ru.feed.items.*
import kekmech.ru.feed.model.FeedModel
import kotlinx.coroutines.*

class FeedPresenter constructor(
    private val model: FeedModel,
    private val context: Context,
    private val router: Router,
    private val updateChecker: UpdateChecker
) : Presenter<IFeedFragment>() {

    var view: IFeedFragment? = null
    val adapter by lazy { BaseAdapter.Builder()
        .registerViewTypeFactory(SessionItem.Factory())
        .registerViewTypeFactory(CarouselItem.Factory())
        .registerViewTypeFactory(EmptyItem.Factory())
        .registerViewTypeFactory(NothingToShowItem.Factory())
        .build()
    }

    /**
     * subscribe to view events
     */
    override fun onResume(view: IFeedFragment) {
        this.view = view

        view.onSettingsClick = { router.navigate(FEED_TO_SETTINGS) }

        view.showLoading()
        adapter.baseItems.clear()
        view.setAdapter(adapter)

        GlobalScope.launch(Dispatchers.Main) {
            // carousel
            withContext(Dispatchers.IO) { model.getCarousel() }.observe(view, Observer { carousel ->
                if (carousel != null) {
                    val notFirstTime = adapter.baseItems.any { it is CarouselItem }
                    if (notFirstTime) {
                        adapter.baseItems[0] = (CarouselItem(carousel, model.getPicasso()))
                        adapter.notifyItemChanged(0)
                    } else {
                        adapter.baseItems.add(0, CarouselItem(carousel, model.getPicasso()))
                        adapter.notifyItemInserted(0)
                    }
                }
            })

            if (withContext(Dispatchers.IO) { model.isSchedulesEmpty }) {
                adapter.baseItems.add(EmptyItem(::onStatusEdit))
                adapter.notifyItemInserted(adapter.baseItems.lastIndex)
                view.hideLoading()
                withContext(Dispatchers.IO) { model.groupNumber }.observe(view, Observer {
                    if (it != null && it.isNotEmpty() && adapter.baseItems.any { e -> e is EmptyItem }) GlobalScope.launch(Dispatchers.Main) {
                        adapter.baseItems.remove(adapter.baseItems.find { e -> e is EmptyItem })
                        adapter.notifyDataSetChanged()
                        view.showLoading()
                        loadAcademicContent(view)
                    }
                })
            } else {
                loadAcademicContent(view)
            }
        }


        // check for updates
        GlobalScope.launch(Dispatchers.Main) {
            delay(1000)
            if (model.isNotShowedUpdateDialog) {
                updateChecker.check { url, desc ->
                    model.saveForceUpdateArgs(url, desc)
                    router.navigate(FEED_TO_FORCE)
                    model.isNotShowedUpdateDialog = false
                }
            }
        }
    }

    private suspend fun loadAcademicContent(view: IFeedFragment) {
        val academicSession = withContext(Dispatchers.IO) { model.getAcademicSession() }
        if (academicSession != null) {
            val item = SessionItem(academicSession)
            adapter.baseItems.add(item)
            adapter.notifyItemInserted(adapter.baseItems.indexOf(item))
        }
        if ((adapter.baseItems.size == 1 && adapter.baseItems.firstOrNull() is CarouselItem) or (adapter.baseItems.isEmpty())) {// если только карусель
            val item = NothingToShowItem()
            adapter.baseItems.add(item)
            adapter.notifyItemInserted(adapter.baseItems.indexOf(item))
        }
        view.hideLoading()
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

}