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
                BarsDeeplinkHandler(get()),
                MainScreenDeeplinkHandler(get()),
                MapDeeplinkHandler(get(), get()),
                ScheduleDeeplinkHandler(get()),
                SearchScreenDeeplinkHandler(get(), get()),
                SettingsScreenDeeplinkHandler(get(), get())
            )
        DeeplinkHandlersProcessor(handlers)
    }
}
