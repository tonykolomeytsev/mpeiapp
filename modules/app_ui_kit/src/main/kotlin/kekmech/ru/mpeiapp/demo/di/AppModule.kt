package kekmech.ru.mpeiapp.demo.di

import kekmech.ru.mpeiapp.demo.screens.elmslie.elm.ElmDemoStoreFactory
import org.koin.dsl.module

val AppModule = module {
    factory { ElmDemoStoreFactory() }
}
