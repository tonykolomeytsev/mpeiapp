package kekmech.ru.domain

import dagger.Binds
import dagger.Module
import kekmech.ru.core.scopes.ActivityScope
import kekmech.ru.core.usecases.*
import kekmech.ru.domain.feed.LoadDayStatusUseCaseImpl
import kekmech.ru.domain.feed.LoadOffsetScheduleUseCaseImpl

@Module
abstract class InteractorModule {

    @Binds
    abstract fun provideLoadOffsetScheduleUseCase(useCaseImpl: LoadOffsetScheduleUseCaseImpl): LoadOffsetScheduleUseCase

    @Binds
    abstract fun provideLoadDayStatusUseCase(useCase: LoadDayStatusUseCaseImpl): LoadDayStatusUseCase

    @Binds
    abstract fun provideSaveSheduleUseCase(useCaseImpl: SaveScheduleUseCaseImpl): SaveScheduleUseCase
}