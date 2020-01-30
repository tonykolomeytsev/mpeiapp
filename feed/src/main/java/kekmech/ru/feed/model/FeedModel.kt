package kekmech.ru.feed.model

import androidx.lifecycle.LiveData
import kekmech.ru.core.dto.*
import kekmech.ru.core.gateways.PicassoFirebaseInstance
import kekmech.ru.coreui.adapter.BaseItem

interface FeedModel {

    var isNotShowedUpdateDialog: Boolean

    /**
     * Group number like "C-12-16"
     */
    val groupNumber: LiveData<String>

    val isNeedToUpdate: LiveData<Boolean>

    val isEvening: Boolean

    val isSemesterStart: Boolean

    /**
     * Get couples for day
     * @param offset - 0 - today, 1 - yesterday etc.
     * @return return today's couples if offset == 0
     */
    fun saveForceUpdateArgs(url: String, description: String)

    fun getAcademicSession(): AcademicSession?
    fun getCarousel(): LiveData<FeedCarousel>
    fun getPicasso(): PicassoFirebaseInstance
    fun getTomorrowSchedule(): LiveData<List<CoupleNative>>
    fun getTodaySchedule(): LiveData<List<CoupleNative>>

    fun updateScheduleFromRemote()
    suspend fun isSchedulesEmpty(): Boolean
}