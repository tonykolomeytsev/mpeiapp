package kekmech.ru.feed.model

import android.util.Log
import kekmech.ru.core.ASYNCIO
import kekmech.ru.core.dto.CoupleNative
import kekmech.ru.core.dto.User
import kekmech.ru.core.scopes.ActivityScope
import kekmech.ru.core.usecases.LoadOffsetScheduleUseCase
import kekmech.ru.core.usecases.LoadUserInfoUseCase
import kekmech.ru.coreui.adapter.BaseItem
import kekmech.ru.coreui.items.DividerItem
import kekmech.ru.feed.items.CoupleItem
import kekmech.ru.feed.items.FeedDividerItem
import kekmech.ru.feed.items.LunchItem
import kekmech.ru.feed.items.WeekendItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.asCoroutineDispatcher
import kotlinx.coroutines.withContext
import java.util.concurrent.Executors
import javax.inject.Inject

@ActivityScope
class FeedModelImpl @Inject constructor(
    val loadOffsetScheduleUseCase: LoadOffsetScheduleUseCase,
    val loadUserInfoUseCase: LoadUserInfoUseCase
) : FeedModel {

    override var scheduleInfoUpdateListener: (List<CoupleNative>) -> Unit = {}

    /**
     * Group number like "C-12-16"
     */
    override val groupNumber: String
        get() = "" //loadUserInfoUseCase.execute(). ?: ""

    /**
     * Current week number
     */
    override val weekNumber: Int
        get() = 0 // FIXME научиться определять номер недели

    var lastOffset = -1

    /**
     * Get couples for day
     * @param offset - 0 - today, 1 - yesterday etc.
     * @return return today's couples if offset == 0
     */
    override suspend fun getDayCouples(offset: Int, refresh: Boolean): List<BaseItem<*>> {
        return withContext(Dispatchers.ASYNCIO) {
            val list = loadOffsetScheduleUseCase.execute(offset, refresh)
            Log.d("OFFSET", "0ffset $offset")
            if (list.isNotEmpty()) {
                val couples = mutableListOf<BaseItem<*>>()
                if (offset > 0) couples += FeedDividerItem("Разделитель", offset == 0)
                list.forEachIndexed { i, e ->
                    // вставим обед между второй и третьей парой
                    if (e.num == 3 && i != 0) {
                        couples += LunchItem()
                    }
                    couples += CoupleItem(e)
                }
                couples
            } else {
                val couples = mutableListOf<BaseItem<*>>()
                if (offset > 0) couples += FeedDividerItem("Разделитель", offset == 0)
                couples += WeekendItem()
                couples
            }
        }
    }

}