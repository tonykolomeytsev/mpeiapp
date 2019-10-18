package kekmech.ru.core

import kekmech.ru.core.usecases.*

interface InteractorProvider {
    fun provideLoadOffsetScheduleUseCase(): LoadOffsetScheduleUseCase
    fun provideLoadGroupNameUseCase(): GetGroupNumberUseCase
    fun provideSaveScheduleUseCase(): SaveScheduleUseCase
    fun provideGetTimetableScheduleUseCase(): GetTimetableScheduleUseCase
    fun provideGetBuildingsUseCase(): GetBuildingsUseCase
    fun provideGetHostelsUseCase(): GetHostelsUseCase
    fun provideGetFoodssUseCase(): GetFoodsUseCase
}