package kekmech.ru.library_navigation.di

import kekmech.ru.library_navigation.NavigationHolder
import kekmech.ru.library_navigation.Router
import kekmech.ru.library_navigation.RouterImpl
import org.koin.dsl.binds
import org.koin.dsl.module

val LibraryNavigationModule = module {
    single { RouterImpl() } binds arrayOf(
        Router::class,
        NavigationHolder::class,
        RouterImpl::class
    )
}
