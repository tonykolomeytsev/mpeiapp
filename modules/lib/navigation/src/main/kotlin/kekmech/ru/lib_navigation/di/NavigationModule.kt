package kekmech.ru.lib_navigation.di

import kekmech.ru.lib_navigation.NavigationHolder
import kekmech.ru.lib_navigation.Router
import kekmech.ru.lib_navigation.RouterImpl
import org.koin.core.module.Module
import org.koin.dsl.binds
import org.koin.dsl.module

public val LibraryNavigationModule: Module = module {
    single { RouterImpl() } binds arrayOf(
        Router::class,
        NavigationHolder::class,
        RouterImpl::class
    )
}
