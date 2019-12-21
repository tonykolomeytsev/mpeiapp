package kekmech.ru.feed.model

import android.content.Context
import androidx.lifecycle.LiveData
import kekmech.ru.core.Router
import kekmech.ru.core.Screens
import kekmech.ru.core.dto.AcademicSession
import kekmech.ru.core.dto.CoupleNative
import kekmech.ru.core.dto.Time
import kekmech.ru.core.scopes.ActivityScope
import kekmech.ru.core.usecases.*
import kekmech.ru.coreui.adapter.BaseItem
import kekmech.ru.feed.R
import kekmech.ru.feed.items.*
import kotlinx.coroutines.*
import javax.inject.Inject

@ActivityScope
class FeedModelImpl @Inject constructor(
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