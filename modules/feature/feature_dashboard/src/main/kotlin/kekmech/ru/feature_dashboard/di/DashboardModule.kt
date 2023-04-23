package kekmech.ru.feature_dashboard.di

import kekmech.ru.common_di.ModuleProvider
import kekmech.ru.domain_dashboard.DashboardFeatureLauncher
import kekmech.ru.feature_dashboard.elm.DashboardActor
import kekmech.ru.feature_dashboard.elm.DashboardFeatureFactory
import kekmech.ru.feature_dashboard.elm.DashboardReducer
import kekmech.ru.feature_dashboard.launcher.DashboardFeatureLauncherImpl
import org.koin.dsl.bind

object DashboardModule : ModuleProvider({
    // I do not remember why this object should be `single`, but this is really necessary
    single { DashboardDependencies(get(), get(), get(), get(), get(), get()) }

    factory { DashboardFeatureFactory(get(), get()) }
    factory { DashboardReducer() }
    factory { DashboardActor(get(), get(), get()) }
    factory { DashboardFeatureLauncherImpl() } bind DashboardFeatureLauncher::class
})
