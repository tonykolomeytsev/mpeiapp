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
}