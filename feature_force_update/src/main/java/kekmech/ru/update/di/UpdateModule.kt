package kekmech.ru.update.di

import kekmech.ru.common_di.ModuleProvider
import kekmech.ru.domain_force_update.ForceUpdateChecker
import kekmech.ru.update.ForceUpdateCheckerImpl
import org.koin.dsl.bind

object ForceUpdateModule : ModuleProvider({
    factory { ForceUpdateCheckerImpl() } bind ForceUpdateChecker::class
})