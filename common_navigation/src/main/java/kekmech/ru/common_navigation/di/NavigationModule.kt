package kekmech.ru.common_navigation.di

import kekmech.ru.common_navigation.NavigationHolder
import kekmech.ru.common_navigation.Router
import kekmech.ru.common_navigation.RouterImpl
import kekmech.ru.common_di.ModuleProvider

object NavigationModule : kekmech.ru.common_di.ModuleProvider({
    single { RouterImpl() } binds arrayOf(
        Router::class,
        NavigationHolder::class,
        RouterImpl::class
    )
})
