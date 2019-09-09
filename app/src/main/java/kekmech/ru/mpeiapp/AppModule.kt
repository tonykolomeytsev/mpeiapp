package kekmech.ru.mpeiapp

import android.content.Context
import dagger.Module
import dagger.Provides
import javax.inject.Singleton


@Module
class AppModule(val context: Context) {
    @Singleton
    @Provides
    fun provideContext(): Context = context
}