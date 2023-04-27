package kekmech.ru.domain_favorite_schedule.di

import kekmech.ru.common_di.ModuleProvider
import kekmech.ru.domain_favorite_schedule.FavoriteScheduleRepository

object DomainFavoriteScheduleModule : ModuleProvider({
    factory { FavoriteScheduleRepository(get()) }
})
