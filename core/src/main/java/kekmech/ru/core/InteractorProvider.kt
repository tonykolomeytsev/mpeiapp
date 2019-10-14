package kekmech.ru.core

import kekmech.ru.core.usecases.*

interface InteractorProvider {
    fun provideLoadOffsetScheduleUseCase(): LoadOffsetScheduleUseCase
    fun provideLoadDayStatusUseCase(): LoadDayStatusUseCase
    fun provideSaveScheduleUseCase(): SaveScheduleUseCase
    fun provideGetTimetableScheduleUseCase(): GetTimetableScheduleUseCase
    fun provideGetBuildingsUseCase(): GetBuildingsUseCase
}