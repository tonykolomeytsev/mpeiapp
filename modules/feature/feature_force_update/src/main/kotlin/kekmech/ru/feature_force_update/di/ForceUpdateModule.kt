package kekmech.ru.feature_force_update.di

import kekmech.ru.common_di.ModuleProvider
import kekmech.ru.domain_force_update.ForceUpdateChecker
import kekmech.ru.feature_force_update.ForceUpdateCheckerImpl
import org.koin.dsl.bind

object ForceUpdateModule : ModuleProvider({
    factory { ForceUpdateCheckerImpl(get()) } bind ForceUpdateChecker::class
})
