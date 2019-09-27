package kekmech.ru.domain.di

import dagger.Binds
import dagger.Module
import kekmech.ru.core.usecases.*
import kekmech.ru.domain.GetTimetableScheduleUseCaseImpl
import kekmech.ru.domain.SaveScheduleUseCaseImpl
import kekmech.ru.domain.LoadDayStatusUseCaseImpl
import kekmech.ru.domain.LoadOffsetScheduleUseCaseImpl

@Module
abstract class InteractorModule {

    @Binds
    abstract fun provideLoadOffsetScheduleUseCase(useCaseImpl: LoadOffsetScheduleUseCaseImpl): LoadOffsetScheduleUseCase

    @Binds
    abstract fun provideLoadDayStatusUseCase(useCase: LoadDayStatusUseCaseImpl): LoadDayStatusUseCase

    @Binds
    abstract fun provideSaveSheduleUseCase(useCaseImpl: SaveScheduleUseCaseImpl): SaveScheduleUseCase

    @Binds
    abstract fun provideGetTimetableScheduleUseCase(useCaseImpl: GetTimetableScheduleUseCaseImpl): GetTimetableScheduleUseCase
}