package kekmech.ru.feed

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import kekmech.ru.core.Router
import kekmech.ru.core.Screens.*
import kekmech.ru.core.UpdateChecker
import kekmech.ru.core.zipNullable
import kekmech.ru.coreui.adapter2.BaseAdapter2
import kekmech.ru.coreui.adapter2.BaseItem2
import kekmech.ru.feed.items.*
import kekmech.ru.feed.model.FeedModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.reflect.KClass

class FeedViewModel constructor(
    private val model: FeedModel,
    private val context: Context,
    private val router: Router,
    private val updateChecker: UpdateChecker
) : ViewModel() {

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

    val adapter by lazy { BaseAdapter2.Builder()
        .registerItemTypes(
            SessionItem::class,
            CarouselItem::class,
            EmptyItem::class,
            NothingToShowItem::class,
            TomorrowCouplesItem::class,
            TodayCouplesItem::class
        ).build()
    }
    val feedItems = Transformations.map(zipNullable(
        model.getCarousel(),
        model.actualSchedule,
        model.getAcademicSession(),
        model.groupNumber)
    ) { (carousel, actualSchedule, sessionSchedule, emptySchedule) ->
        val newListOfItems = mutableListOf<BaseItem2<*>>()

        val isLoadingLocal = (carousel == null) || (actualSchedule == null && sessionSchedule == null && emptySchedule == null)
        isLoading.value = isLoadingLocal
        if (carousel != null) newListOfItems.add(CarouselItem(carousel, model.getPicasso()))
        if (!actualSchedule.isNullOrEmpty())
            newListOfItems.add(if (model.isEvening) TomorrowCouplesItem(actualSchedule) else TodayCouplesItem(actualSchedule))
        if (sessionSchedule != null ) newListOfItems.add(SessionItem(sessionSchedule))
        if (emptySchedule.isNullOrEmpty()) newListOfItems.add(EmptyItem(::navigateToAdd))
        if (newListOfItems.isEmpty() || newListOfItems.all { it is CarouselItem })
            if (!isLoadingLocal) newListOfItems.add(NothingToShowItem())

        newListOfItems.sortedByClass(if (model.isSemesterStart) onStartSemesterOrder else onEndSemesterOrder)
    }
    val isLoading = MutableLiveData<Boolean>().apply { value = true }

    fun checkForUpdates() {
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

    fun checkForEmptyChedule() = model.checkIsSchedulesEmpty()

    fun navigateToAdd() = router.navigate(FEED_TO_ADD)

    fun navigateToSettings() = router.navigate(FEED_TO_SETTINGS)

    fun updateSchedules() = model.updateScheduleFromRemote()

    fun updateActualSchedules() = model.updateActualSchedule()

    private fun List<BaseItem2<*>>.sortedByClass(sortOrder: List<KClass<out BaseItem2<*>>>) =
        this.sortedBy { sortOrder.indexOf(it::class).let { i -> if (i == -1) 999 else i } }

}