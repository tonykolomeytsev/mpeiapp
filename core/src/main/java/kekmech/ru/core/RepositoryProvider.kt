package kekmech.ru.core

import io.realm.Realm
import kekmech.ru.core.repositories.ScheduleRepository
import kekmech.ru.core.repositories.UserRepository

interface RepositoryProvider {
    fun provideRealm(): Realm
    fun provideUserRepository(): UserRepository
    fun provideScheduleRepository(): ScheduleRepository
}