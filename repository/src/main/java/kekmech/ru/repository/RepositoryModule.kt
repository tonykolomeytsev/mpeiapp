package kekmech.ru.repository

import dagger.Module
import dagger.Provides
import io.realm.Realm
import kekmech.ru.core.Repository
import javax.inject.Singleton

@Module
class RepositoryModule {

    @Provides
    @Singleton
    fun provideRealm(): Realm = Realm.getDefaultInstance()

    @Provides
    @Singleton
    fun provideRepository(realm: Realm): Repository = RepositoryImpl(realm)

}