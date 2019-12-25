package kekmech.ru.feed.model

import android.content.Context
import androidx.lifecycle.LiveData
import kekmech.ru.core.dto.AcademicSession
import kekmech.ru.core.usecases.*

class FeedModelImpl constructor(
    private val context: Context,
    private val getGroupNumberUseCase: GetGroupNumberUseCase,
    private val isNeedToUpdateFeedUseCase: IsNeedToUpdateFeedUseCase,
    private val setNeedToUpdateFeedUseCase: SetNeedToUpdateFeedUseCase,
    private val setForceUpdateDataUseCase: SetForceUpdateDataUseCase,
    private val setIsShowedUpdateDialogUseCase: SetIsShowedUpdateDialogUseCase,
    private val getIsShowedUpdateDialogUseCase: GetIsShowedUpdateDialogUseCase,
    private val isSchedulesEmptyUseCase: IsSchedulesEmptyUseCase,
    private val getAcademicSessionUseCase: GetAcademicSessionUseCase
) : FeedModel {

    override val isSchedulesEmpty: Boolean
        get() = isSchedulesEmptyUseCase()

    /**
     * Group number like "C-12-16"
     */
    override val groupNumber: LiveData<String> get() = getGroupNumberUseCase()

    override val isNeedToUpdate: LiveData<Boolean>
        get() = isNeedToUpdateFeedUseCase()

    override var isNotShowedUpdateDialog: Boolean
        get() = getIsShowedUpdateDialogUseCase()
        set(value) { setIsShowedUpdateDialogUseCase(value) }

    override fun saveForceUpdateArgs(url: String, description: String) {
        setForceUpdateDataUseCase(url, description)
    }

    override fun nitifyFeedUpdated() {
        setNeedToUpdateFeedUseCase(false)
    }

    override fun getAcademicSession(): AcademicSession? {
        return getAcademicSessionUseCase()
    }
}