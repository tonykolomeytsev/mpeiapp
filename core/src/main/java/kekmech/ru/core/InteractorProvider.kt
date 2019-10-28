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
    fun provideNeedUpdateUseCase(): IsNeedToUpdateFeedUseCase
    fun provideSetNeedUpdateUseCase(): SetNeedToUpdateFeedUseCase
    fun provideForceUpdateUseCase(): SetForceUpdateDataUseCase
    fun provideGetForceUpdateUseCase(): GetForceUpdateDataUseCase
    fun provideIncrementAppLaunchCountUseCase(): IncrementAppLaunchCountUseCase
    fun provideGetAppLaunchCountUseCase(): GetAppLaunchCountUseCase
    fun provideGetAllShedulesUseCase(): GetAllSchedulesUseCase
    fun provideChangeCurrentScheduleIdUseCase(): ChangeCurrentScheduleIdUseCase
    fun provideGetMapStateUseCase(): GetMapStateUseCase
    fun provideSetMapStateUseCase(): SetMapStateUseCase
}