package kekmech.ru.domain

import dagger.Binds
import dagger.Module
import kekmech.ru.core.scopes.ActivityScope
import kekmech.ru.core.scopes.FeedScope
import kekmech.ru.core.scopes.FragmentScope
import kekmech.ru.core.usecases.LoadOffsetScheduleUseCase
import kekmech.ru.domain.feed.LoadOffsetScheduleUseCaseImpl

@Module
abstract class InteractorModule() {

    @ActivityScope
    @Binds
    abstract fun provideLoadOffsetScheduleUseCase(useCaseImpl: LoadOffsetScheduleUseCaseImpl): LoadOffsetScheduleUseCase
}