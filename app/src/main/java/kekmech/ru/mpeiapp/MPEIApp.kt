package kekmech.ru.mpeiapp

import android.app.Application
import kekmech.ru.common_navigation.Router
import kekmech.ru.common_navigation.di.NavigationModule
import kekmech.ru.common_navigation.di.RouterHolder
import kekmech.ru.core.usecases.GetAppVersionUseCase
import kekmech.ru.domain.di.KoinDomainModule
import kekmech.ru.mainscreen.di.KoinMainActivityModule
import kekmech.ru.repository.di.KoinRepositoryModule
import org.koin.android.ext.android.inject
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.Koin
import org.koin.core.KoinApplication
import org.koin.core.context.startKoin
import org.koin.dsl.bind
import org.koin.dsl.module


class MPEIApp : Application(),
    RouterHolder {

    override val router by inject<Router>()

    override fun onCreate() {
        super.onCreate()
        RemoteConfig.setup()
        initKoin()
    }

    private fun initKoin() = startKoin {
        androidLogger()
        androidContext(this@MPEIApp)
        modules(listOf(
            KoinMainActivityModule,
            KoinRepositoryModule,
            KoinDomainModule,
            NavigationModule.provider,
            module {
                single { GetAppVersionUseCaseImpl() } bind GetAppVersionUseCase::class
            }
        ))
    }

    override fun getPackageName(): String {
        try {
            val stackTrace = Thread.currentThread().stackTrace
            for (element in stackTrace) {
                if ("org.chromium.base.BuildInfo".equals(element.className, true)) {
                    if ("getAll".equals(element.methodName, ignoreCase = true))
                        return ""
                    break
                }
            }
        } catch (e: Exception) { }
        return super.getPackageName()
    }
}