package kekmech.ru.feed.model

import androidx.lifecycle.LiveData
import kekmech.ru.core.dto.CoupleNative
import kekmech.ru.core.dto.Time
import kekmech.ru.core.dto.User
import kekmech.ru.coreui.adapter.BaseItem

interface FeedModel {

    val today: Time

    var scheduleInfoUpdateListener: (List<CoupleNative>) -> Unit
    /**
     * Group number like "C-12-16"
     */
    val groupNumber: String

    /**
     * Current week number
     */
    val currentWeekNumber: Int

    val formattedTodayStatus: String

    /**
     * For off day stack
     */
    var weekendOffset: Int

    /**
     * Get couples for day
     * @param offset - 0 - today, 1 - yesterday etc.
     * @return return today's couples if offset == 0
     */
    suspend fun getDayCouples(offset: Int, refresh: Boolean): List<BaseItem<*>>
}