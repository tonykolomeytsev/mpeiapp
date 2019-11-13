package kekmech.ru.domain

import kekmech.ru.core.repositories.UserRepository
import kekmech.ru.core.usecases.SetIsShowedUpdateDialogUseCase
import javax.inject.Inject

class SetIsShowedUpdateDialogUseCaseImpl @Inject constructor(
    private val userRepository: UserRepository
) : SetIsShowedUpdateDialogUseCase {
    override fun invoke(boolean: Boolean) {
        userRepository.isShowedUpdateDialog = boolean
    }
}