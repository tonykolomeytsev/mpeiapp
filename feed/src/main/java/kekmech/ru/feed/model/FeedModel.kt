package kekmech.ru.feed.model

import androidx.lifecycle.LiveData
import kekmech.ru.core.dto.AcademicSession
import kekmech.ru.core.dto.CoupleNative
import kekmech.ru.core.dto.Time
import kekmech.ru.core.dto.User
import kekmech.ru.coreui.adapter.BaseItem

interface FeedModel {

    val isSchedulesEmpty: Boolean

    var isNotShowedUpdateDialog: Boolean

    /**
     * Group number like "C-12-16"
     */
    val groupNumber: LiveData<String>

    val isNeedToUpdate: LiveData<Boolean>

    /**
     * Get couples for day
     * @param offset - 0 - today, 1 - yesterday etc.
     * @return return today's couples if offset == 0
     */
    fun saveForceUpdateArgs(url: String, description: String)

    fun nitifyFeedUpdated()

    fun getAcademicSession(): AcademicSession?
}