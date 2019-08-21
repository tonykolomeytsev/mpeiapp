package kekmech.ru.repository.di

import dagger.Binds
import dagger.Module
import kekmech.ru.core.repositories.ScheduleRepository
import kekmech.ru.core.repositories.UserRepository
import kekmech.ru.repository.ScheduleRepositoryImpl
import kekmech.ru.repository.UserRepositoryImpl

@Module
abstract class RepositoryModule {

    @Binds
    abstract fun provideScheduleRepository(repoImpl: ScheduleRepositoryImpl): ScheduleRepository

    @Binds
    abstract fun provideUserRepository(repoImpl: UserRepositoryImpl): UserRepository

}