package kekmech.ru.domain

import kekmech.ru.core.repositories.UserRepository
import kekmech.ru.core.usecases.GetIsShowedUpdateDialogUseCase

class GetIsShowedUpdateDialogUseCaseImpl constructor(
    private val userRepository: UserRepository
) : GetIsShowedUpdateDialogUseCase {
    override fun invoke(): Boolean {
        return userRepository.isShowedUpdateDialog
    }
}