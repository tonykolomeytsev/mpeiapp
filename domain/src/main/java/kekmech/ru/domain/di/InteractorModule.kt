package kekmech.ru.domain.di

import dagger.Binds
import dagger.Module
import kekmech.ru.core.usecases.*
import kekmech.ru.domain.*

@Module
abstract class InteractorModule {

    @Binds
    abstract fun provideLoadOffsetScheduleUseCase(useCaseImpl: LoadOffsetScheduleUseCaseImpl): LoadOffsetScheduleUseCase

    @Binds
    abstract fun provideSaveSheduleUseCase(useCaseImpl: SaveScheduleUseCaseImpl): SaveScheduleUseCase

    @Binds
    abstract fun provideGetTimetableScheduleUseCase(useCaseImpl: GetTimetableScheduleUseCaseImpl): GetTimetableScheduleUseCase

    @Binds
    abstract fun provideGetBuildingsUseCase(useCaseImpl: GetBuildingsUseCaseImpl): GetBuildingsUseCase

    @Binds
    abstract fun provideGetHostelsUseCase(useCaseImpl: GetHostelsUseCaseImpl): GetHostelsUseCase

    @Binds
    abstract fun provideGetFoodsUseCase(useCaseImpl: GetFoodsUseCaseImpl): GetFoodsUseCase

    @Binds
    abstract fun provideGetGroupNameUseCase(useCaseImpl: GetGroupNumberUseCaseImpl): GetGroupNumberUseCase

    @Binds
    abstract fun provideUpdateFeedUseCase(useCaseImpl: IsNeedToUpdateFeedUseCaseImpl): IsNeedToUpdateFeedUseCase

    @Binds
    abstract fun provideSetUpdateFeedUseCase(useCaseImpl: SetNeedToUpdateFeedUseCaseImpl): SetNeedToUpdateFeedUseCase

    @Binds
    abstract fun provideSetForceUpdateUseCase(useCaseImpl: SetForceUpdateDataUseCaseImpl): SetForceUpdateDataUseCase

    @Binds
    abstract fun provideGetForceUpdateUseCase(useCaseImpl: GetForceUpdateDataUseCaseImpl): GetForceUpdateDataUseCase

    @Binds
    abstract fun provideIncrementAppLaunchCountUseCase(useCaseImpl: IncrementAppLaunchCountUseCaseImpl): IncrementAppLaunchCountUseCase

    @Binds
    abstract fun provideGetAppLaunchCountUseCase(useCaseImpl: GetAppLaunchCountUseCaseImpl): GetAppLaunchCountUseCase

    @Binds
    abstract fun provideGetAllSchedulesUseCase(useCaseImpl: GetAllSchedulesUseCaseImpl): GetAllSchedulesUseCase

    @Binds
    abstract fun provideChangeCurrentScheduleIdUseCase(useCaseImpl: ChangeCurrentScheduleIdUseCaseImpl): ChangeCurrentScheduleIdUseCase

    @Binds
    abstract fun provideGetMapStateUseCase(useCaseImpl: GetMapStateUseCaseImpl): GetMapStateUseCase

    @Binds
    abstract fun provideSetMapStateUseCase(useCaseImpl: SetMapStateUseCaseImpl): SetMapStateUseCase

    @Binds
    abstract fun provideIsLoggedInBarsUseCase(useCaseImpl: IsLoggedInBarsUseCaseImpl): IsLoggedInBarsUseCase

    @Binds
    abstract fun provideSaveUserSecretsUseCase(useCaseImpl: SaveUserSecretsUseCaseImpl): SaveUserSecretsUseCase

    @Binds
    abstract fun provideGetRatingUseCase(useCaseImpl: GetRatingUseCaseImpl): GetRatingUseCase

    @Binds
    abstract fun provideLogOutUseCase(useCaseImpl: LogOutUseCaseImpl): LogOutUseCase

    @Binds
    abstract fun provideSetDetailsDisciplineUseCase(useCaseImpl: SetDetailsDisciplineUseCaseImpl): SetDetailsDisciplineUseCase

    @Binds
    abstract fun provideGetDetailsDisciplineUseCase(useCaseImpl: GetDetailsDisciplineUseCaseImpl): GetDetailsDisciplineUseCase

    @Binds
    abstract fun provideSetIsShowedUpdateDialogUseCase(useCaseImpl: SetIsShowedUpdateDialogUseCaseImpl): SetIsShowedUpdateDialogUseCase

    @Binds
    abstract fun provideGetIsShowedUpdateDialogUseCase(useCaseImpl: GetIsShowedUpdateDialogUseCaseImpl): GetIsShowedUpdateDialogUseCase
}