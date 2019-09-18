package kekmech.ru.feed.model

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kekmech.ru.core.dto.CoupleNative
import kekmech.ru.core.dto.Time
import kekmech.ru.core.scopes.ActivityScope
import kekmech.ru.core.usecases.LoadDayStatusUseCase
import kekmech.ru.core.usecases.LoadOffsetScheduleUseCase
import kekmech.ru.coreui.adapter.BaseItem
import kekmech.ru.feed.R
import kekmech.ru.feed.items.CoupleItem
import kekmech.ru.feed.items.FeedDividerItem
import kekmech.ru.feed.items.LunchItem
import kekmech.ru.feed.items.WeekendItem
import kotlinx.coroutines.*
import javax.inject.Inject

@ActivityScope
class FeedModelImpl @Inject constructor(
    private val context: Context,
    private val loadOffsetScheduleUseCase: LoadOffsetScheduleUseCase,
    private val loadDayStatusUseCase: LoadDayStatusUseCase
) : FeedModel {

    override val today: Time
        get() = Time.today()

    override var scheduleInfoUpdateListener: (List<CoupleNative>) -> Unit = {}

    /**
     * Group number like "C-12-16"
     */
    override val groupNumber: String
        get() = loadDayStatusUseCase.execute(0).groupNum

    /**
     * Current week number
     */
    override val currentWeekNumber: Int
        get() = today.weekOfSemester

    override val formattedTodayStatus: String
        get() = "${today.formattedAsDayName(context, R.array.days_of_week)}, ${today.dayOfMonth} " +
                today.formattedAsMonthName(context, R.array.months)

    /**
     * Get couples for day
     * @param offset - 0 - today, 1 - yesterday etc.
     * @return return today's couples if offset == 0
     */
    override suspend fun getDayCouples(offset: Int, refresh: Boolean): List<BaseItem<*>> {
        return withContext(Dispatchers.IO) {
            val list = loadOffsetScheduleUseCase.execute(offset, refresh)
            if (list.isNotEmpty()) {
                val couples = mutableListOf<BaseItem<*>>()
                if (offset > 0) couples += FeedDividerItem(
                    today.getDayWithOffset(offset).formatAsDivider(),
                    offset == 0
                )
                list.forEachIndexed { i, e ->
                    // вставим обед между второй и третьей парой
                    if (e.num == 3 && i != 0) {
                        couples += LunchItem()
                    }
                    couples += CoupleItem(e).apply {
                        isDividerVisible = (i != list.size - 1)
                    }
                }
                couples
            } else {
                val couples = mutableListOf<BaseItem<*>>()
                if (offset > 0) couples += FeedDividerItem(
                    today.getDayWithOffset(offset).formatAsDivider(),
                    offset == 0
                )
                couples += WeekendItem()
                couples
            }
        }
    }

    private fun Time.formatAsDivider() =
        "${formattedAsDayName(context, R.array.days_of_week)}, $dayOfMonth " +
                formattedAsMonthName(context, R.array.months)

}