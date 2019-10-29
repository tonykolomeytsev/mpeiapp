package kekmech.ru.repository.di

import dagger.Binds
import dagger.Module
import kekmech.ru.core.repositories.BarsRepository
import kekmech.ru.core.repositories.PlacesRepository
import kekmech.ru.core.repositories.ScheduleRepository
import kekmech.ru.core.repositories.UserRepository
import kekmech.ru.repository.BarsRepositoryImpl
import kekmech.ru.repository.PlacesRepositoryImpl
import kekmech.ru.repository.ScheduleRepositoryImpl
import kekmech.ru.repository.UserRepositoryImpl
import javax.inject.Singleton

@Module
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun provideScheduleRepository(repoImpl: ScheduleRepositoryImpl): ScheduleRepository

    @Binds
    @Singleton
    abstract fun provideUserRepository(repoImpl: UserRepositoryImpl): UserRepository

    @Binds
    @Singleton
    abstract fun providePlacesRepository(repoImpl: PlacesRepositoryImpl): PlacesRepository

    @Binds
    @Singleton
    abstract fun provideBarsRepository(repoImpl: BarsRepositoryImpl): BarsRepository

}