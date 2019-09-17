package kekmech.ru.core

import kekmech.ru.core.usecases.LoadDayStatusUseCase
import kekmech.ru.core.usecases.LoadOffsetScheduleUseCase
import kekmech.ru.core.usecases.LoadUserInfoUseCase
import kekmech.ru.core.usecases.SaveScheduleUseCase

interface InteractorProvider {
    fun provideLoadOffsetScheduleUseCase(): LoadOffsetScheduleUseCase
    fun provideLoadUserInfoUseCase(): LoadUserInfoUseCase
    fun provideLoadDayStatusUseCase(): LoadDayStatusUseCase
    fun provideSaveScheduleUseCase(): SaveScheduleUseCase
}