package kekmech.ru.feed.presenter

import android.content.Context
import androidx.lifecycle.Observer
import kekmech.ru.core.Presenter
import kekmech.ru.core.Router
import kekmech.ru.core.Screens.*
import kekmech.ru.core.UpdateChecker
import kekmech.ru.coreui.adapter.BaseSortedAdapter
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

    private val onStartSemesterOrder = listOf(
        CarouselItem::class, // карусель с новостями с Firebase
        TomorrowCouplesItem::class, // расписание на завтра
        TodayCouplesItem::class,
        SessionItem::class, // расписание сессии

        /* вспомогательные */
        NothingToShowItem::class, // показывается если нету инета или произошла ошибка
        EmptyItem::class // показывается если нету расписаний (не выбрана группа)
    )
    private val onEndSemesterOrder = listOf(
        CarouselItem::class, // карусель с новостями с Firebase
        SessionItem::class, // расписание сессии
        TomorrowCouplesItem::class, // расписание на завтра
        TodayCouplesItem::class,

        /* вспомогательные */
        NothingToShowItem::class, // показывается если нету инета или произошла ошибка
        EmptyItem::class // показывается если нету расписаний (не выбрана группа)
    )


    var view: IFeedFragment? = null
    val adapter by lazy { BaseSortedAdapter.Builder()
        .registerViewTypeFactory(SessionItem.Factory())
        .registerViewTypeFactory(CarouselItem.Factory())
        .registerViewTypeFactory(EmptyItem.Factory())
        .registerViewTypeFactory(NothingToShowItem.Factory())
        .registerViewTypeFactory(TomorrowCouplesItem.Factory())
        .registerViewTypeFactory(TodayCouplesItem.Factory())
        .addItemsOrder(if (model.isSemesterStart) onStartSemesterOrder else onEndSemesterOrder)
        .allowOnlyUniqueItems()
        .build()
    }

    /**
     * subscribe to view events
     */
    override fun onResume(view: IFeedFragment) {
        this.view = view

        model.updateScheduleFromRemote()
        view.onSettingsClick = { router.navigate(FEED_TO_SETTINGS) }

        view.showLoading()
        //adapter.baseItems.clear()
        view.setAdapter(adapter)

        GlobalScope.launch(Dispatchers.Main) {
            // carousel
            withContext(Dispatchers.IO) { model.getCarousel() }.observe(view, Observer { carousel ->
                if (carousel != null && carousel.items.isNotEmpty()) {
                    adapter.addItem(CarouselItem(carousel, model.getPicasso()))
                }
            })

            // если у нас нету расписаний
            if (withContext(Dispatchers.IO) { model.isSchedulesEmpty() }) {
                adapter.addItem(EmptyItem(::onStatusEdit)) // то покажем плиточку с выбором расписания
                view.hideLoading()

                // и будем дожидаться пока кто-то введет расписание
                model.groupNumber.observe(view, Observer {
                    // если нам прилетел номер группы, и есть карточка EmptyItem в ленте
                    // PS номер группы поменяется только если расписание будет загружено
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
        val tasks = listOf(
            async {
                val academicSession = withContext(Dispatchers.IO) { model.getAcademicSession() }
                if (academicSession != null)
                    adapter.addItem(SessionItem(academicSession))
            },
            async {
                if (model.isEvening) {
                    adapter.removeItemByClass(TodayCouplesItem::class)
                    model.getTomorrowSchedule().observe(view, Observer {
                        if (it != null && it.isNotEmpty()) adapter.addItem(TomorrowCouplesItem(it))
                    })
                } else {
                    adapter.removeItemByClass(TomorrowCouplesItem::class)
                    model.getTodaySchedule().observe(view, Observer {
                        if (it != null && it.isNotEmpty()) adapter.addItem(TodayCouplesItem(it))
                    })
                }
            }
        )

        tasks.awaitAll()
        if ((adapter.itemCount == 1 && adapter.baseItems.firstOrNull() is CarouselItem) or (adapter.baseItems.isEmpty())) {// если только карусель
            adapter.addItem(NothingToShowItem())
        } else {
            adapter.removeItemByClass(NothingToShowItem::class)
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

    private fun<T> async(action: suspend CoroutineScope.() -> T) = GlobalScope.async(Dispatchers.Main, block = action)

    /**
     * unsubscribe to view events
     */
    override fun onPause(view: IFeedFragment) {
        this.view = null
    }

}