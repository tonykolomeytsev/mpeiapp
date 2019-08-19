package kekmech.ru.domain

import dagger.Binds
import dagger.Module
import kekmech.ru.core.scopes.ActivityScope
import kekmech.ru.core.usecases.LoadOffsetScheduleUseCase
import kekmech.ru.core.usecases.LoadUserInfoUseCase
import kekmech.ru.domain.feed.LoadOffsetScheduleUseCaseImpl
import kekmech.ru.domain.feed.LoadUserInfoUseCaseImpl

@Module
abstract class InteractorModule() {

    @Binds
    abstract fun provideLoadOffsetScheduleUseCase(useCaseImpl: LoadOffsetScheduleUseCaseImpl): LoadOffsetScheduleUseCase

    @Binds
    abstract fun provideLoadUserInfoUseCase(useCaseImpl: LoadUserInfoUseCaseImpl): LoadUserInfoUseCase
}