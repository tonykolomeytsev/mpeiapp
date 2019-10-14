package kekmech.ru.core

import kekmech.ru.core.repositories.PlacesRepository
import kekmech.ru.core.repositories.ScheduleRepository
import kekmech.ru.core.repositories.UserRepository

interface RepositoryProvider {
    fun provideUserRepository(): UserRepository
    fun provideScheduleRepository(): ScheduleRepository
    fun providePlacesRepository(): PlacesRepository
}