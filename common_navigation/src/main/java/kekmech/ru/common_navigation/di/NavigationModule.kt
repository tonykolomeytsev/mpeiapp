package kekmech.ru.common_navigation.di

import kekmech.ru.common_navigation.NavigationHolder
import kekmech.ru.common_navigation.Router
import kekmech.ru.common_navigation.RouterImpl
import kekmech.ru.core.ModuleProvider
import org.koin.dsl.binds

object NavigationModule : ModuleProvider({
    single { RouterImpl() } binds arrayOf(
        Router::class,
        NavigationHolder::class,
        RouterImpl::class
    )
})
