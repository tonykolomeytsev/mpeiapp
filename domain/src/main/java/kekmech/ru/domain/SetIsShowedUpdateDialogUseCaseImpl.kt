package kekmech.ru.domain

import kekmech.ru.core.repositories.UserRepository
import kekmech.ru.core.usecases.SetIsShowedUpdateDialogUseCase

class SetIsShowedUpdateDialogUseCaseImpl constructor(
    private val userRepository: UserRepository
) : SetIsShowedUpdateDialogUseCase {
    override fun invoke(boolean: Boolean) {
        userRepository.isShowedUpdateDialog = boolean
    }
}