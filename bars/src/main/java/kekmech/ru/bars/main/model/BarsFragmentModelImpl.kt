package kekmech.ru.bars.main.model

import kekmech.ru.core.usecases.IsLoggedInBarsUseCase
import kekmech.ru.core.usecases.SaveUserSecretsUseCase
import javax.inject.Inject

class BarsFragmentModelImpl @Inject constructor(
    private val isLoggedInBarsUseCase: IsLoggedInBarsUseCase,
    private val saveUserSecretsUseCase: SaveUserSecretsUseCase
) : BarsFragmentModel {

    override val isLoggedIn: Boolean
        get() = isLoggedInBarsUseCase()

    override fun logInUser(login: String, pass: String) {
        saveUserSecretsUseCase(login, pass)
    }
}