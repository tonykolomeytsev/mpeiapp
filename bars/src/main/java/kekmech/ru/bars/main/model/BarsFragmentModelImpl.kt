package kekmech.ru.bars.main.model

import androidx.lifecycle.LiveData
import kekmech.ru.core.dto.AcademicDiscipline
import kekmech.ru.core.dto.AcademicScore
import kekmech.ru.core.usecases.*

class BarsFragmentModelImpl constructor(
    private val isLoggedInBarsUseCase: IsLoggedInBarsUseCase,
    private val saveUserSecretsUseCase: SaveUserSecretsUseCase,
    private val getRatingUseCase: GetRatingUseCase,
    private val logOutUseCase: LogOutUseCase,
    private val setDetailsDisciplineUseCase: SetDetailsDisciplineUseCase,
    private val setForceUpdateDataUseCase: SetForceUpdateDataUseCase,
    private val getIsShowedUpdateDialogUseCase: GetIsShowedUpdateDialogUseCase,
    private val setIsShowedUpdateDialogUseCase: SetIsShowedUpdateDialogUseCase,
    private val getRatingLiveDataUseCase: GetRatingLiveDataUseCase,
    private val updateRatingUseCase: UpdateRatingUseCase,
    private val getLoginScriptUseCase: GetLoginScriptUseCase,
    private val hasUserCredentialsUseCase: HasUserCredentialsUseCase,
    private val setUserAgentUseCase: SetUserAgentUseCase
) : BarsFragmentModel {


    override var isNotShowedUpdateDialog: Boolean
        get() = getIsShowedUpdateDialogUseCase()
        set(value) { setIsShowedUpdateDialogUseCase(value) }

    override val isLoggedIn: LiveData<Boolean>
        get() = isLoggedInBarsUseCase()

    override var ratingDetails: AcademicScore.Rating? = null

    override val score: LiveData<AcademicScore>
        get() = getRatingLiveDataUseCase()

    override var realUpdateIndex: Int = 0

    override var lastUpdateIndex: Int = -1

    override val hasUserCredentials: Boolean
        get() = hasUserCredentialsUseCase()

    override fun setUserAgent(ua: String) {
        setUserAgentUseCase(ua)
    }

    override fun saveUserSecrets(login: String, pass: String) {
        saveUserSecretsUseCase(login, pass)
    }

    override fun getLoginScript() = getLoginScriptUseCase()

    override suspend fun getAcademicScore(refresh: Boolean): AcademicScore? {
        return getRatingUseCase(refresh)
    }

    override suspend fun getAcademicScoreAsync(refresh: Boolean, onRatingUpdatesListener: (AcademicScore?) -> Unit) {
        if (!refresh) onRatingUpdatesListener(getRatingUseCase())
        onRatingUpdatesListener(getRatingUseCase(true))
    }

    override fun logout() {
        logOutUseCase()
    }

    override fun setCurrentDiscipline(discipline: AcademicDiscipline) {
        setDetailsDisciplineUseCase(discipline)
    }

    override fun saveForceUpdateArgs(url: String, description: String) {
        setForceUpdateDataUseCase(url, description)
    }

    override suspend fun updateScore() {
        realUpdateIndex++
        updateRatingUseCase()
    }
}