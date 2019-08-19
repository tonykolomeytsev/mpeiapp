package kekmech.ru.core

import kekmech.ru.core.usecases.LoadOffsetScheduleUseCase
import kekmech.ru.core.usecases.LoadUserInfoUseCase

interface InteractorProvider {
    fun provideLoadOffsetScheduleUseCase(): LoadOffsetScheduleUseCase
    fun provideLoadUserInfoUseCase(): LoadUserInfoUseCase
}