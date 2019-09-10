package kekmech.ru.feed.model

import kekmech.ru.core.ASYNCIO
import kekmech.ru.core.dto.CoupleNative
import kekmech.ru.core.scopes.ActivityScope
import kekmech.ru.core.usecases.LoadDayStatusUseCase
import kekmech.ru.core.usecases.LoadOffsetScheduleUseCase
import kekmech.ru.coreui.adapter.BaseItem
import kekmech.ru.feed.items.CoupleItem
import kekmech.ru.feed.items.FeedDividerItem
import kekmech.ru.feed.items.LunchItem
import kekmech.ru.feed.items.WeekendItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

@ActivityScope
class FeedModelImpl @Inject constructor(
    private val loadOffsetScheduleUseCase: LoadOffsetScheduleUseCase,
    private val loadDayStatusUseCase: LoadDayStatusUseCase
) : FeedModel {

    override var scheduleInfoUpdateListener: (List<CoupleNative>) -> Unit = {}

    /**
     * Group number like "C-12-16"
     */
    override val groupNumber: String
        get() = "C-12-16" //loadUserInfoUseCase.execute(). ?: ""

    /**
     * Current week number
     */
    override val currentWeekNumber: Int
        get() = loadDayStatusUseCase.execute(0).time.weekOfSemester

    /**
     * Get couples for day
     * @param offset - 0 - today, 1 - yesterday etc.
     * @return return today's couples if offset == 0
     */
    override suspend fun getDayCouples(offset: Int, refresh: Boolean): List<BaseItem<*>> {
        return withContext(Dispatchers.ASYNCIO) {
            val list = loadOffsetScheduleUseCase.execute(offset, refresh)
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