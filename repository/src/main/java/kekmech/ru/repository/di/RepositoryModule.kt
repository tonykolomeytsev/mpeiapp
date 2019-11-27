package kekmech.ru.repository.di

import dagger.Binds
import dagger.Module
import kekmech.ru.core.repositories.*
import kekmech.ru.repository.*
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

    @Binds
    @Singleton
    abstract fun provideNotesRepository(repoImpl: NotesRepositoryImpl): NotesRepository

}