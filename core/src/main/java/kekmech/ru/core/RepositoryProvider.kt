package kekmech.ru.core

import kekmech.ru.core.repositories.*

interface RepositoryProvider {
    fun provideUserRepository(): UserRepository
    fun provideScheduleRepository(): ScheduleRepository
    fun providePlacesRepository(): PlacesRepository
    fun provideBarsRepository(): BarsRepository
    fun provideNotesRepository(): NotesRepository
}