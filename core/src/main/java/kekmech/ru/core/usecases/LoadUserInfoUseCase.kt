package kekmech.ru.core.usecases

import kekmech.ru.core.UseCase
import kekmech.ru.core.dto.User

interface LoadUserInfoUseCase : UseCase<Unit, User>