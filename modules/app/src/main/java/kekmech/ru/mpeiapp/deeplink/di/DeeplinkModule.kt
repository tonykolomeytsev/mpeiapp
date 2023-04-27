package kekmech.ru.mpeiapp.deeplink.di

import kekmech.ru.mpeiapp.deeplink.DeeplinkHandler
import kekmech.ru.mpeiapp.deeplink.DeeplinkHandlersProcessor
import kekmech.ru.mpeiapp.deeplink.handlers.BarsDeeplinkHandler
import kekmech.ru.mpeiapp.deeplink.handlers.MainScreenDeeplinkHandler
import kekmech.ru.mpeiapp.deeplink.handlers.MapDeeplinkHandler
import kekmech.ru.mpeiapp.deeplink.handlers.ScheduleDeeplinkHandler
import kekmech.ru.mpeiapp.deeplink.handlers.SearchScreenDeeplinkHandler
import kekmech.ru.mpeiapp.deeplink.handlers.SettingsScreenDeeplinkHandler
import org.koin.dsl.module

val DeeplinkModule = module {
    factory {
        val handlers =
            listOf<DeeplinkHandler>(
                BarsDeeplinkHandler(get(), get()),
                MainScreenDeeplinkHandler(get(), get()),
                MapDeeplinkHandler(get(), get(), get()),
                ScheduleDeeplinkHandler(get(), get()),
                SearchScreenDeeplinkHandler(get(), get(), get()),
                SettingsScreenDeeplinkHandler(get(), get(), get())
            )
        DeeplinkHandlersProcessor(handlers)
    }
}
