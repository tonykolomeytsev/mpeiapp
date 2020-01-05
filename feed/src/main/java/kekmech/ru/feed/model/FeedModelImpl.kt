package kekmech.ru.feed.model

import android.content.Context
import androidx.lifecycle.LiveData
import kekmech.ru.core.dto.AcademicSession
import kekmech.ru.core.dto.CoupleNative
import kekmech.ru.core.dto.FeedCarousel
import kekmech.ru.core.gateways.PicassoFirebaseInstance
import kekmech.ru.core.usecases.*

class FeedModelImpl constructor(
    private val context: Context,
    private val getGroupNumberUseCase: GetGroupNumberUseCase,
    private val isNeedToUpdateFeedUseCase: IsNeedToUpdateFeedUseCase,
    private val setForceUpdateDataUseCase: SetForceUpdateDataUseCase,
    private val setIsShowedUpdateDialogUseCase: SetIsShowedUpdateDialogUseCase,
    private val getIsShowedUpdateDialogUseCase: GetIsShowedUpdateDialogUseCase,
    private val isSchedulesEmptyUseCase: IsSchedulesEmptyUseCase,
    private val getAcademicSessionUseCase: GetAcademicSessionUseCase,
    private val getFeedCarouselUseCase: GetFeedCarouselUseCase,
    private val getPicassoInstanceUseCase: GetPicassoInstanceUseCase,
    private val getTomorrowCouplesUseCase: GetTomorrowCouplesUseCase,
    private val getTodayCouplesUseCase: GetTodayCouplesUseCase,
    private val isEveningUseCase: IsEveningUseCase,
    private val isSemesterStartUseCase: IsSemesterStartUseCase
) : FeedModel {

    override val isSchedulesEmpty: Boolean
        get() = isSchedulesEmptyUseCase()

    /**
     * Group number like "C-12-16"
     */
    override val groupNumber: LiveData<String> get() = getGroupNumberUseCase()

    override val isNeedToUpdate: LiveData<Boolean>
        get() = isNeedToUpdateFeedUseCase()

    override val isEvening: Boolean
        get() = isEveningUseCase()

    override val isSemesterStart: Boolean
        get() = isSemesterStartUseCase()

    override var isNotShowedUpdateDialog: Boolean
        get() = getIsShowedUpdateDialogUseCase()
        set(value) { setIsShowedUpdateDialogUseCase(value) }

    override fun saveForceUpdateArgs(url: String, description: String) {
        setForceUpdateDataUseCase(url, description)
    }
    
    override fun getAcademicSession(): AcademicSession? {
        return getAcademicSessionUseCase()
    }

    override fun getCarousel(): LiveData<FeedCarousel> {
        return getFeedCarouselUseCase()
    }

    override fun getPicasso(): PicassoFirebaseInstance {
        return getPicassoInstanceUseCase()
    }

    override fun getTomorrowSchedule(): List<CoupleNative> {
        return getTomorrowCouplesUseCase()
    }

    override fun getTodaySchedule(): List<CoupleNative> {
        return getTodayCouplesUseCase()
    }
}