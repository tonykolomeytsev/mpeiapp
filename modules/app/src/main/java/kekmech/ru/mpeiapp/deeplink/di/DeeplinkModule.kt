package kekmech.ru.mpeiapp.deeplink.di

import kekmech.ru.common_di.ModuleProvider
import kekmech.ru.mpeiapp.deeplink.DeeplinkHandler
import kekmech.ru.mpeiapp.deeplink.DeeplinkHandlersProcessor
import kekmech.ru.mpeiapp.deeplink.handlers.*

object DeeplinkModule : ModuleProvider({
    factory {
        val handlers = listOf<DeeplinkHandler>(
            BarsDeeplinkHandler(get(), get()),
            MainScreenDeeplinkHandler(get(), get()),
            MapDeeplinkHandler(get(), get(), get()),
            ScheduleDeeplinkHandler(get(), get()),
            SearchScreenDeeplinkHandler(get(), get(), get()),
            SettingsScreenDeeplinkHandler(get(), get(), get())
        )
        DeeplinkHandlersProcessor(handlers)
    }
})
