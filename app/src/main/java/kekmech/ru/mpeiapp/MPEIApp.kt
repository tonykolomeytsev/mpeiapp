package kekmech.ru.mpeiapp

import android.app.Application
import kekmech.ru.core.usecases.GetAppVersionUseCase
import kekmech.ru.domain.di.KoinDomainModule
import kekmech.ru.mainscreen.di.KoinMainActivityModule
import kekmech.ru.repository.di.KoinRepositoryModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.dsl.bind
import org.koin.dsl.module

class MPEIApp : Application() {
    override fun onCreate() {
        super.onCreate()
        RemoteConfig.setup()
        startKoin {
            androidLogger()
            androidContext(this@MPEIApp)
            modules(listOf(
                KoinMainActivityModule,
                KoinRepositoryModule,
                KoinDomainModule,
                module {
                    single { GetAppVersionUseCaseImpl() } bind GetAppVersionUseCase::class
                }
            ))
        }
    }
}