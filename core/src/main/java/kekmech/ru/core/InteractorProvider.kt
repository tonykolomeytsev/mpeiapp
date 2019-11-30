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
    fun provideIsLoggedInBarsUseCase(): IsLoggedInBarsUseCase
    fun provideSaveUserSecretsUseCase(): SaveUserSecretsUseCase
    fun provideGetRatingUseCase(): GetRatingUseCase
    fun provideLogOutUseCase(): LogOutUseCase
    fun provideSetDetailsDisciplineUseCase(): SetDetailsDisciplineUseCase
    fun provideGetDetailsDisciplineUseCase(): GetDetailsDisciplineUseCase
    fun provideSetIsShowedUpdateDialogUseCase(): SetIsShowedUpdateDialogUseCase
    fun provideGetIsShowedUpdateDialogUseCase(): GetIsShowedUpdateDialogUseCase
    fun provideGetNoteByTimeUseCase(): GetNoteByTimeUseCase
    fun provideRemoveNoteUseCase(): RemoveNoteUseCase
    fun provideSaveNoteUseCase(): SaveNoteUseCase
    fun provideSetCreateNoteTransactionUseCase(): SetCreateNoteTransactionUseCase
    fun provideGetCreateNoteTransactionUseCase(): GetCreateNoteTransactionUseCase
    fun provideGetNoteByIdUseCase(): GetNoteByIdUseCase
}