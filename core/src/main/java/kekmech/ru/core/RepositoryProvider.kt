package kekmech.ru.core

import io.realm.Realm

interface RepositoryProvider {
    fun provideRealm(): Realm
}