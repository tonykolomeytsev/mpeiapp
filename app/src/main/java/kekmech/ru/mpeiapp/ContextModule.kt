package kekmech.ru.mpeiapp

import android.content.Context
import dagger.Module
import dagger.Provides
import javax.inject.Singleton


@Module
class ContextModule(var context: Context) {

    @Provides
    fun provideContext(): Context {
        return context
    }
}