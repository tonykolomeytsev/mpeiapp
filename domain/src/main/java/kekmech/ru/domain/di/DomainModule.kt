package kekmech.ru.domain.di

import kekmech.ru.common_di.ModuleProvider
import kekmech.ru.core.usecases.*
import kekmech.ru.domain.*

object DomainModule : ModuleProvider({
    single { GetBuildingsUseCaseImpl(get()) } bind GetBuildingsUseCase::class
    single { GetLoginScriptUseCaseImpl(get()) } bind GetLoginScriptUseCase::class
    single { HasUserCredentialsUseCaseImpl(get()) } bind HasUserCredentialsUseCase::class

    single { GetFoodsUseCaseImpl(get()) } bind GetFoodsUseCase::class
    single { GetHostelsUseCaseImpl(get()) } bind GetHostelsUseCase::class
    single { GetGroupNumberUseCaseImpl(get()) } bind GetGroupNumberUseCase::class

    single { SetForceUpdateDataUseCaseImpl(get()) } bind SetForceUpdateDataUseCase::class
    single { GetForceUpdateDataUseCaseImpl(get()) } bind GetForceUpdateDataUseCase::class
    single { IncrementAppLaunchCountUseCaseImpl(get()) } bind IncrementAppLaunchCountUseCase::class

    single { GetAppLaunchCountUseCaseImpl(get()) } bind GetAppLaunchCountUseCase::class
    single { GetAllSchedulesUseCaseImpl(get()) } bind GetAllSchedulesUseCase::class
    single { ChangeCurrentScheduleUseCaseImpl(get()) } bind ChangeCurrentScheduleUseCase::class

    single { GetMapStateUseCaseImpl(get()) } bind GetMapStateUseCase::class
    single { SetMapStateUseCaseImpl(get()) } bind SetMapStateUseCase::class
    single { IsLoggedInBarsUseCaseImpl(get()) } bind IsLoggedInBarsUseCase::class

    single { SaveUserSecretsUseCaseImpl(get()) } bind SaveUserSecretsUseCase::class
    single { GetRatingUseCaseImpl(get()) } bind GetRatingUseCase::class
    single { LogOutUseCaseImpl(get()) } bind LogOutUseCase::class

    single { SaveNoteUseCaseImpl(get()) } bind SaveNoteUseCase::class
    single { RemoveNoteUseCaseImpl(get()) } bind RemoveNoteUseCase::class
    single { GetNoteByTimestampUseCaseImpl(get()) } bind GetNoteByTimestampUseCase::class

    single { SetDetailsDisciplineUseCaseImpl(get()) } bind SetDetailsDisciplineUseCase::class
    single { GetDetailsDisciplineUseCaseImpl(get()) } bind GetDetailsDisciplineUseCase::class
    single { SetIsShowedUpdateDialogUseCaseImpl(get()) } bind SetIsShowedUpdateDialogUseCase::class

    single { GetIsShowedUpdateDialogUseCaseImpl(get()) } bind GetIsShowedUpdateDialogUseCase::class
    single { SetCreateNoteTransactionUseCaseImpl(get()) } bind SetCreateNoteTransactionUseCase::class
    single { GetCreateNoteTransactionUseCaseImpl(get()) } bind GetCreateNoteTransactionUseCase::class

    single { GetNoteByIdUseCaseImpl(get()) } bind GetNoteByIdUseCase::class
    single { IsSchedulesEmptyUseCaseImpl(get()) } bind IsSchedulesEmptyUseCase::class
    single { GetAcademicSessionUseCaseImpl(get()) } bind GetAcademicSessionUseCase::class

    single { GetFeedCarouselUseCaseImpl(get()) } bind GetFeedCarouselUseCase::class
    single { GetPicassoInstanceUseCaseImpl(get()) } bind GetPicassoInstanceUseCase::class
    single { RemoveAllSchedulesUseCaseImpl(get(), get()) } bind RemoveAllSchedulesUseCase::class

    single { RemoveAllNotesUseCaseImpl(get()) } bind RemoveAllNotesUseCase::class
    single { GetTomorrowCouplesUseCaseImpl(get()) } bind GetTomorrowCouplesUseCase::class
    single { IsDarkThemeEnabledUseCaseImpl(get()) } bind IsDarkThemeEnabledUseCase::class

    single { SetDarkThemeEnabledUseCaseImpl(get()) } bind SetDarkThemeEnabledUseCase::class
    single { IsSemesterStartUseCaseImpl() } bind IsSemesterStartUseCase::class
    single { IsEveningUseCaseImpl() } bind IsEveningUseCase::class

    single { GetTodayCouplesUseCaseImpl(get()) } bind GetTodayCouplesUseCase::class
    single { InvokeUpdateScheduleUseCaseImpl(get()) } bind InvokeUpdateScheduleUseCase::class
    single { LoadNewScheduleUseCaseImpl(get()) } bind LoadNewScheduleUseCase::class

    single { GetRatingLiveDataUseCaseImpl(get()) } bind GetRatingLiveDataUseCase::class
    single { UpdateRatingUseCaseImpl(get()) } bind UpdateRatingUseCase::class
    single { GetTimetableScheduleLiveDataUseCaseImpl(get()) } bind GetTimetableScheduleLiveDataUseCase::class

    single { SetUserAgentUseCaseImpl(get()) } bind SetUserAgentUseCase::class
})