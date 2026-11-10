package kekmech.ru.feature_bars_impl.di

import io.github.tonykolomeytsev.mpeix.sdk.LogLevel
import io.github.tonykolomeytsev.mpeix.sdk.MpeixSdk
import io.github.tonykolomeytsev.mpeix.sdk.MpeixSdkConfig
import io.github.tonykolomeytsev.mpeix.sdk.SdkLogger
import io.github.tonykolomeytsev.mpeix.sdk.provider.MpeixSdkProvider
import kekmech.ru.ext_koin.bindIntoList
import kekmech.ru.feature_bars_api.BarsLogoutHandle
import kekmech.ru.feature_bars_impl.data.repository.BarsProfileIdRepository
import kekmech.ru.feature_bars_impl.data.sdk.SdkCrashlyticsEventSink
import kekmech.ru.feature_bars_impl.navigation.BarsNavEntries
import kekmech.ru.feature_bars_impl.presentation.screen.accounts.BarsAccountsViewModel
import kekmech.ru.feature_bars_impl.presentation.screen.details.BarsDetailsViewModel
import kekmech.ru.feature_bars_impl.presentation.screen.login.BarsLoginViewModel
import kekmech.ru.feature_bars_impl.presentation.screen.main.elm.BarsActor
import kekmech.ru.feature_bars_impl.presentation.screen.main.elm.BarsStoreFactory
import kekmech.ru.lib_app_info.AppVersionName
import kekmech.ru.lib_navigation.NavEntries
import org.koin.android.ext.koin.androidApplication
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.binds
import org.koin.dsl.module
import timber.log.Timber

val FeatureBarsModule = module {
    factoryOf(::BarsActor)
    singleOf(::BarsStoreFactory)
    viewModelOf(::BarsAccountsViewModel)
    viewModelOf(::BarsDetailsViewModel)
    viewModelOf(::BarsLoginViewModel)

    singleOf(::BarsProfileIdRepository) binds arrayOf(BarsProfileIdRepository::class, BarsLogoutHandle::class)

    single<MpeixSdk> {
        MpeixSdkProvider.create(
            MpeixSdkConfig {
                storageDirectory = androidApplication().filesDir.resolve("mpeix-sdk").path
                appVersion = get<AppVersionName>().versionName
                logger = SdkLogger { level, tag, message, error ->
                    Timber.tag(tag).log(level.priority, error, message)
                }
                eventSink = SdkCrashlyticsEventSink()
            }
        )
    }
    factoryOf(::BarsNavEntries) bindIntoList NavEntries::class
}

private val LogLevel.priority: Int
    get() = when (this) {
        LogLevel.Verbose -> android.util.Log.VERBOSE
        LogLevel.Debug -> android.util.Log.DEBUG
        LogLevel.Info -> android.util.Log.INFO
        LogLevel.Warning -> android.util.Log.WARN
        LogLevel.Error -> android.util.Log.ERROR
    }
