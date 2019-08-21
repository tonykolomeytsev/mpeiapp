package kekmech.ru.repository

import dagger.Module
import dagger.Provides
import io.realm.Realm
import javax.inject.Singleton

@Module
class RealmModule {

    @Provides
    @Singleton
    fun provideRealm(): Realm = Realm.getDefaultInstance()

}