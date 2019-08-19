package kekmech.ru.domain.feed

import kekmech.ru.core.dto.User
import kekmech.ru.core.usecases.LoadUserInfoUseCase
import kekmech.ru.domain.DaggerInteractorComponent
import javax.inject.Inject

class LoadUserInfoUseCaseImpl @Inject constructor() : LoadUserInfoUseCase {
    override fun init(i: Unit) {

    }

    override fun execute(): User {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}