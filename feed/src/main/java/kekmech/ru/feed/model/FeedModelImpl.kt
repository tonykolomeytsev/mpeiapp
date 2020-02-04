package kekmech.ru.feed.model

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import kekmech.ru.core.dto.AcademicSession
import kekmech.ru.core.dto.CoupleNative
import kekmech.ru.core.dto.FeedCarousel
import kekmech.ru.core.gateways.PicassoFirebaseInstance
import kekmech.ru.core.usecases.*
import kekmech.ru.core.zipNullable
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main

class FeedModelImpl constructor(
    private val getGroupNumberUseCase: GetGroupNumberUseCase,
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
    private val isSemesterStartUseCase: IsSemesterStartUseCase,
    private val invokeUpdateScheduleUseCase: InvokeUpdateScheduleUseCase
) : FeedModel {

    /**
     * Group number like "C-12-16"
     */
    override val groupNumber: LiveData<String> get() = getGroupNumberUseCase()

    override val isEvening: Boolean
        get() = isEveningUseCase()

    override val isSemesterStart: Boolean
        get() = isSemesterStartUseCase()

    override var isNotShowedUpdateDialog: Boolean
        get() = getIsShowedUpdateDialogUseCase()
        set(value) { setIsShowedUpdateDialogUseCase(value) }

    override val isSchedulesEmpty = MutableLiveData<Boolean>()

    override fun saveForceUpdateArgs(url: String, description: String) {
        setForceUpdateDataUseCase(url, description)
    }
    
    override fun getAcademicSession(): LiveData<AcademicSession> {
        return getAcademicSessionUseCase()
    }

    override fun getCarousel(): LiveData<FeedCarousel> {
        return getFeedCarouselUseCase()
    }

    override fun getPicasso(): PicassoFirebaseInstance {
        return getPicassoInstanceUseCase()
    }

    override fun getTomorrowSchedule(): LiveData<List<CoupleNative>> {
        return getTomorrowCouplesUseCase()
    }

    override fun getTodaySchedule(): LiveData<List<CoupleNative>> {
        return getTodayCouplesUseCase()
    }

    private var isNotUpdated = true
    override fun updateScheduleFromRemote() {
        if (isNotUpdated) GlobalScope.launch(Dispatchers.IO) {
            var attempts = 0
            while (isNotUpdated && attempts < 5) {
                try {
                    invokeUpdateScheduleUseCase()
                    isNotUpdated = false
                    Log.d("FeedModel", "ScheduleUpdated")
                } catch (e: Exception) {
                    e.printStackTrace()
                }
                attempts++
                delay(1000)
            }
        }
    }

    private val isEveningLiveData = MutableLiveData<Boolean>().apply { value = isEvening }
    override val actualSchedule: LiveData<List<CoupleNative>> = Transformations.switchMap(isEveningLiveData) {
        if (it == true) getTomorrowSchedule() else getTodaySchedule()
    }

    override fun checkIsSchedulesEmpty() {
        GlobalScope.launch(Main) {
            isSchedulesEmpty.value = withContext(IO) { isSchedulesEmptyUseCase() }
        }
    }

    override fun updateActualSchedule() {
        isEveningLiveData.value = isEvening
    }
}