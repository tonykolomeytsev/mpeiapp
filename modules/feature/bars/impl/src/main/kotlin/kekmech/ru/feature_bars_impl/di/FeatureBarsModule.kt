package kekmech.ru.feature_bars_impl.di

import com.kekmech.lib_bars.BarsHandle
import com.kekmech.lib_bars.DataStore
import kekmech.ru.feature_bars_api.BarsFeatureLauncher
import kekmech.ru.feature_bars_api.BarsLogoutHandle
import kekmech.ru.feature_bars_impl.data.datasource.BarsConfigDataSource
import kekmech.ru.feature_bars_impl.data.datasource.BarsExtractJsDataSource
import kekmech.ru.feature_bars_impl.data.network.BarsService
import kekmech.ru.feature_bars_impl.data.repository.BarsConfigRepository
import kekmech.ru.feature_bars_impl.data.repository.BarsExtractJsRepository
import kekmech.ru.feature_bars_impl.data.repository.BarsRepository
import kekmech.ru.feature_bars_impl.data.repository.BarsUserInfoRepository
import kekmech.ru.feature_bars_impl.launcher.BarsFeatureLauncherImpl
import kekmech.ru.feature_bars_impl.presentation.screen.login.elm.BarsLoginActor
import kekmech.ru.feature_bars_impl.presentation.screen.login.elm.BarsLoginStoreFactory
import kekmech.ru.feature_bars_impl.presentation.screen.main_compose.elm.BarsComposeActor
import kekmech.ru.feature_bars_impl.presentation.screen.main_compose.elm.BarsComposeStoreFactory
import kekmech.ru.feature_bars_impl.data.network.BarsTwoFactorSessionHolder
import kekmech.ru.lib_network.AppEnvironment
import kekmech.ru.lib_network.ServiceUrlResolver
import kekmech.ru.lib_network.buildApi
import okhttp3.Interceptor
import okhttp3.logging.HttpLoggingInterceptor
import okhttp3.logging.HttpLoggingInterceptor.Level
import org.koin.android.ext.koin.androidApplication
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.binds
import org.koin.dsl.module
import retrofit2.Retrofit

val FeatureBarsModule = module {
    factoryOf(::BarsComposeActor)
    factoryOf(::BarsLoginActor)
    singleOf(::BarsComposeStoreFactory)
    singleOf(::BarsLoginStoreFactory)
    factoryOf(::BarsFeatureLauncherImpl) bind BarsFeatureLauncher::class

    singleOf(::BarsRepository) binds arrayOf(BarsRepository::class, BarsLogoutHandle::class)
    factoryOf(::BarsConfigDataSource)
    factoryOf(::BarsConfigRepository)
    factoryOf(::BarsExtractJsDataSource)
    factoryOf(::BarsExtractJsRepository)
    factoryOf(::BarsUserInfoRepository)
    single { get<Retrofit.Builder>().buildApi<BarsService>() } bind BarsService::class

    single { DataStore(androidApplication().dataDir) }
    single {
        BarsTwoFactorSessionHolder.init(androidApplication())
        val loggingInterceptor = if (ServiceUrlResolver.getAppEnvironment() != AppEnvironment.PROD) {
            getAll<Interceptor>().find { it is HttpLoggingInterceptor }
                ?: HttpLoggingInterceptor().apply { level = Level.BODY }
        } else {
            null
        }
        BarsHandle(androidApplication(), get()) {
            addInterceptor(kekmech.ru.feature_bars_impl.data.network.BarsTwoFactorCompatInterceptor())
            addNetworkInterceptor(kekmech.ru.feature_bars_impl.data.network.BarsTwoFactorCompatInterceptor())
            loggingInterceptor?.let(::addNetworkInterceptor)
            this
        }
    }
}
