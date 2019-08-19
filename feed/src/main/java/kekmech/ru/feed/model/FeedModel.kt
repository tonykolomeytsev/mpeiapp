package kekmech.ru.feed.model

import kekmech.ru.coreui.adapter.BaseItem

interface FeedModel {
    /**
     * Group number like "C-12-16"
     */
    val groupNumber: String

    /**
     * Current week number
     */
    val weekNumber: Int

    /**
     * Get couples for day
     * @param offset - 0 - today, 1 - yesterday etc.
     * @return return today's couples if offset == 0
     */
    suspend fun getDayCouples(offset: Int): List<BaseItem<*>>
}