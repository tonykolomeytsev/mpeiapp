package kekmech.ru.mpeiapp

import android.content.Context
import dagger.Module
import dagger.Provides
import ru.terrakok.cicerone.Cicerone
import ru.terrakok.cicerone.Router
import javax.inject.Singleton
import ru.terrakok.cicerone.NavigatorHolder



@Module
class AppModule(val context: Context, val cicerone: Cicerone<Router>) {
    @Singleton
    @Provides
    fun provideCicerone(): Cicerone<Router> = cicerone

    @Singleton
    @Provides
    fun provideCiceroneNavigatorHolder(): NavigatorHolder = cicerone.navigatorHolder

    @Singleton
    @Provides
    fun provideCiceroneRouter(): Router = cicerone.router

    @Singleton
    @Provides
    fun provideContext(): Context = context
}