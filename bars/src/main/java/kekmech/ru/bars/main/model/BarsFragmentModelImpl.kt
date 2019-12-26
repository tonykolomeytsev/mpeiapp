package kekmech.ru.bars.main.model

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
    private val setIsShowedUpdateDialogUseCase: SetIsShowedUpdateDialogUseCase
) : BarsFragmentModel {


    override var isNotShowedUpdateDialog: Boolean
        get() = getIsShowedUpdateDialogUseCase()
        set(value) { setIsShowedUpdateDialogUseCase(value) }

    override val isLoggedIn: Boolean
        get() = isLoggedInBarsUseCase()

    override var ratingDetails: AcademicScore.Rating? = null

    override fun saveUserSecrets(login: String, pass: String) {
        saveUserSecretsUseCase(login, pass)
    }

    override suspend fun getAcademicScore(refresh: Boolean): AcademicScore? {
        return getRatingUseCase(refresh)
    }

    override suspend fun getAcademicScoreAsync(refresh: Boolean, onRatingUpdatesListener: (AcademicScore?) -> Unit) {
        if (!refresh) onRatingUpdatesListener(getRatingUseCase())
        onRatingUpdatesListener(getRatingUseCase(true))
    }

    override fun clearUserSecrets() {
        logOutUseCase()
    }

    override fun setCurrentDiscipline(discipline: AcademicDiscipline) {
        setDetailsDisciplineUseCase(discipline)
    }

    override fun saveForceUpdateArgs(url: String, description: String) {
        setForceUpdateDataUseCase(url, description)
    }
}