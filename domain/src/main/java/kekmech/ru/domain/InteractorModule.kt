package kekmech.ru.domain

import dagger.Binds
import dagger.Module
import kekmech.ru.core.scopes.ActivityScope
import kekmech.ru.core.usecases.*
import kekmech.ru.domain.feed.LoadDayStatusUseCaseImpl
import kekmech.ru.domain.feed.LoadOffsetScheduleUseCaseImpl
import kekmech.ru.domain.feed.LoadUserInfoUseCaseImpl
import kekmech.ru.domain.feed.LoadWeekInfoUseCaseImpl

@Module
abstract class InteractorModule {

    @Binds
    abstract fun provideLoadOffsetScheduleUseCase(useCaseImpl: LoadOffsetScheduleUseCaseImpl): LoadOffsetScheduleUseCase

    @Binds
    abstract fun provideLoadUserInfoUseCase(useCaseImpl: LoadUserInfoUseCaseImpl): LoadUserInfoUseCase

    @Binds
    abstract fun provideLoadWeekInfoUseCase(useCaseImpl: LoadWeekInfoUseCaseImpl): LoadWeekInfoUseCase

    @Binds
    abstract fun provideLoadDayStatusUseCase(useCase: LoadDayStatusUseCaseImpl): LoadDayStatusUseCase

    @Binds
    abstract fun provideSaveSheduleUseCase(useCaseImpl: SaveScheduleUseCaseImpl): SaveScheduleUseCase
}