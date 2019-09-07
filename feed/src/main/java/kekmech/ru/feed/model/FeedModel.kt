package kekmech.ru.feed.model

import kekmech.ru.core.dto.CoupleNative
import kekmech.ru.core.dto.User
import kekmech.ru.coreui.adapter.BaseItem

interface FeedModel {

    var scheduleInfoUpdateListener: (List<CoupleNative>) -> Unit
    /**
     * Group number like "C-12-16"
     */
    val groupNumber: String

    /**
     * Current week number
     */
    val currentWeekNumber: Int

    /**
     * Get couples for day
     * @param offset - 0 - today, 1 - yesterday etc.
     * @return return today's couples if offset == 0
     */
    suspend fun getDayCouples(offset: Int, refresh: Boolean): List<BaseItem<*>>
}