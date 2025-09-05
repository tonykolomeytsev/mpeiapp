package kekmech.ru.common_navigation.di

import kekmech.ru.common_navigation.NavigationHolder
import kekmech.ru.common_navigation.Router
import kekmech.ru.common_navigation.RouterImpl
import org.koin.core.module.Module
import org.koin.dsl.binds
import org.koin.dsl.module

public val CommonNavigationModule: Module = module {
    single { RouterImpl() } binds arrayOf(
        Router::class,
        NavigationHolder::class,
        RouterImpl::class
    )
}
